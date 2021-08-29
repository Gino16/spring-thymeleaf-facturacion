package com.sistema.facturacion;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FacturacionApplication {
    private static final String ACCESS_TOKEN = "sl.A3coi6dYLk2bEQekIiSw9IiwdUAZboQa62TyLfl3Wiv1qNyXZlkksjjCGYcgiOYZAd6Rfoc47R-Jv_0NSMb9WsIkW_8YzuiRVTw1iPO1JiJDj3nKhBbDOx1cKQNz8umBkdZshyw";

    public static void main(String[] args) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/prueba-factura-spring").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        SpringApplication.run(FacturacionApplication.class, args);
    }

}
