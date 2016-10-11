package command;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import websocket.WsHandler;

@Controller
public class NConfiguration {

	final String confFileName = "conf.json";
	final String nenesHome = ".nenes";
	final String userHome = System.getProperty("user.home");
	final static String separator = System.getProperty("file.separator");

	public static Configuration conf;
	
	@Autowired
	WebApplicationContext applicationContext;

	public NConfiguration() {
		conf = readConfigFile_Gson();
		
		// workspace 디렉토리 생성
		File workspaceDir = new File(userHome + separator + nenesHome + separator + "workspace");
		if (!workspaceDir.exists()) {
			System.out.println("creating directory: " + userHome + separator + nenesHome + separator + "workspace");
			workspaceDir.mkdir();
		}
		
		//System.out.println(readConfigFile_Gson().getFileDirectory());
	}

	@RequestMapping("/nconfiguration")
	public String SaveConfiguration(Configuration configuratlon, Model model) {

		System.out.println("입력 Configuration : " + configuratlon.getFileDirectory());
		
		 WsHandler ws = (WsHandler)applicationContext.getBean("wsHandler");
		//ws.sendMessage("하하하");
		//System.out.println("nene.service.wsHandler : " + ws);

		if (configuratlon.getFileDirectory() == null) {
			System.out.println("getFileDirectory : " + this.conf.getFileDirectory());
			model.addAttribute("configuration", this.conf);
			return "nconfiguration";
		}

		this.conf = configuratlon;
		saveConfigFile_Gson(configuratlon);
		model.addAttribute("configuration", configuratlon);
		return "nconfiguration";
	}

	private Configuration readConfigFile_Gson() {

		Gson gson = new Gson();

		Configuration newConf = null;
		/*
		 * try { Configuration configuratlon = gson.fromJson(new
		 * FileReader(confFile), Configuration.class); return configuratlon; }
		 * catch (FileNotFoundException e) { e.printStackTrace(); }
		 */

		try {

			File confFile = ResourceUtils.getFile(userHome + separator + nenesHome + separator + confFileName);

			if (confFile.exists()) {
				// file:~/.nenes/conf.json classpath:conf.json
				Configuration configuratlon = gson.fromJson(new FileReader(confFile), Configuration.class);
				return configuratlon;
			} else {

				newConf = new Configuration();
				newConf.setFileDirectory(userHome + separator + nenesHome + separator + "workspace");
				String r = gson.toJson(newConf);

				// 디렉토리 생성
				File theDir = new File(userHome + separator + nenesHome);

				// if the directory does not exist, create it
				if (!theDir.exists()) {
					System.out.println("creating directory: " + userHome + separator + nenesHome);
					theDir.mkdir();
				}
				// 디렉토리 생성 끝

				// 파일생성
				File newFile = new File(userHome + separator + nenesHome + separator + confFileName);
				System.out.println(userHome + separator + nenesHome + separator + confFileName);
				FileWriter w = new FileWriter(newFile);
				w.write(r);
				w.flush();
				w.close();
				System.out.println("creating conf.json : " + userHome + separator + nenesHome + separator + confFileName);
				// 파일생성끝
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return newConf;
	}

	private void saveConfigFile_Gson(Configuration configuratlon) {
		Gson gson = new Gson();
		String result = gson.toJson(configuratlon);

		try {
			FileWriter file = new FileWriter(ResourceUtils.getFile(userHome + separator + nenesHome + separator + confFileName));
			file.write(result);
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}