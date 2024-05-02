package es.uvigo.tfg.valvi.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uvigo.tfg.valvi.dto.ReducedVideogameDto;
import es.uvigo.tfg.valvi.dto.VideogameDto;
import es.uvigo.tfg.valvi.dto.filters.VideogameFiltering;
import es.uvigo.tfg.valvi.entity.Videogame;
import es.uvigo.tfg.valvi.mapper.VideogameMapper;
import es.uvigo.tfg.valvi.repository.VideogameRepository;
import es.uvigo.tfg.valvi.service.VideogameService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * The type Videogame service.
 */
@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class VideogameServiceImpl implements VideogameService {
  
  @NonNull
  private VideogameRepository videogameRepository;
  @NonNull
  private VideogameMapper videogameMapper;
  
  @NonNull
  private ObjectMapper objectMapper;
  
  @Override
  public List<VideogameDto> findVideogames(VideogameFiltering videogameFiltering){
    return null;
  }

  @Override
  public VideogameDto upsertVideogame(VideogameDto videogameDto){
    Videogame videogame = this.videogameRepository.save(this.videogameMapper.toVideogame(videogameDto));
    return this.videogameMapper.toVideogameDto(videogame);
  }
  
  @Override
  public List<ReducedVideogameDto> getReducedSteamVideogames() throws IOException {
    String baseUrl = "api.steampowered.com";
    String urlId = "/ISteamApps/GetAppList/v2";
    URL obj = new URL("https", baseUrl, urlId);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    int responseCode = con.getResponseCode();
    System.out.println("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      
      String arrayJson = response.substring(19, response.length()-2);

      return this.objectMapper.readValue(arrayJson, new TypeReference<List<ReducedVideogameDto>>(){});
    } else {
      System.out.println("GET request did not work.");
    }
    
    return null;
  }

  @Override
  public VideogameDto getSteamVideogame(Integer id) throws IOException {
    String baseUrl = "store.steampowered.com";
    String urlId = "/api/appdetails?appids=" + id;
    URL obj = new URL("https", baseUrl, urlId);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("appids", String.valueOf(id));
    int responseCode = con.getResponseCode();
    System.out.println("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      VideogameDto dto = this.objectMapper.readValue(response.toString().substring(29, response.length()-2), VideogameDto.class);
      
      return dto;
    } else {
      System.out.println("GET request did not work.");
    }

    return null;
  }
  @Override
  public Integer deleteVideogame(Integer id){
    this.videogameRepository.deleteById(id);
    return id;
  }
  
}
