package com.initgrep.cr.msauth.security.keypair.impl;

import com.initgrep.cr.msauth.config.AppConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class KeyPairVaultTestUtil {

    public static AppConfig.KeyPairConfig getKeyPairConfig() {
        AppConfig.KeyPairConfig keyPairConfig = new AppConfig.KeyPairConfig();
        keyPairConfig.setDirectory("token-keys-test");
        return keyPairConfig;
    }

  public static AppConfig.AccessTokenConfig getAccessTokenConfig(){
      AppConfig.AccessTokenConfig accessTokenConfig = new AppConfig.AccessTokenConfig();
      accessTokenConfig.setPublicKeyPath("token-keys-test/access-token-public.key");
      accessTokenConfig.setPrivateKeyPath("token-keys-test/access-token-private.key");
      accessTokenConfig.setExpiryDurationMin(5);
      return accessTokenConfig;
  }

  public static AppConfig.RefreshTokenConfig getRefreshTokenConfig(){
      AppConfig.RefreshTokenConfig refreshTokenConfig = new AppConfig.RefreshTokenConfig();
      refreshTokenConfig.setPublicKeyPath("token-keys-test/refresh-token-public.key");
      refreshTokenConfig.setPrivateKeyPath("token-keys-test/refresh-token-private.key");
      refreshTokenConfig.setExpiryDurationDays(30);
      refreshTokenConfig.setMinRenewalDays(7);
      return refreshTokenConfig;
  }

  public static void cleanTestDirectory(AppConfig appConfig) throws IOException {
      Path parentDirPath = Path.of(appConfig.getKeyPair().getDirectory());
      File parentDir = new File(appConfig.getKeyPair().getDirectory());
      if (Files.isDirectory(parentDirPath) && Files.exists(parentDirPath)) {
          String[] subDirs = parentDir.list();
          if (Objects.nonNull(subDirs)) {
              for (String subPath : subDirs) {
                  Files.delete(parentDirPath.resolve(subPath));
              }
          }
          Files.delete(parentDirPath);
      }
  }



}
