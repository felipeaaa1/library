# Publicando uma Imagem Docker no Docker Hub

## 1. Objetivo

Evitar que a imagem seja criada apenas localmente e garantir que ela seja enviada diretamente para sua conta no Docker Hub.

---

## 2. Login no Docker Hub no Terminal

```powershell
# Login com o usuário correto
# Substitua pela sua senha ou token de acesso pessoal (PAT)
docker login -u felipeaaa
```

> Se o login não for feito antes do build, a imagem será local e não poderá ser enviada diretamente ao Docker Hub.

---

## 3. Build da Imagem com Nome Correto

```powershell
# Build com o nome da imagem vinculado ao seu usuário do Docker Hub (sempre em lowercase)
docker build -t felipeaaa/library_api:1.0.0 .
```

> Nomes como `felipeAAA` ou `libraryAPI` não são válidos, pois Docker exige lowercase.

---

## 4. Push para o Docker Hub

```powershell
# Envia a imagem para o repositório remoto
docker push felipeaaa/library_api:1.0.0
```

> Após o login correto, o push deve funcionar. Se der erro de permissão, verifique se o repositório foi criado e se o nome/tag está correto.

---

## 5. Resultado Esperado

```powershell
The push refers to repository [docker.io/felipeaaa/library_api]
[...] # camadas sendo enviadas
1.0.0: digest: sha256:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx size: NNN
```

A imagem ficará visível em:
🔗 [https://hub.docker.com/repository/docker/felipeaaa/library\_api](https://hub.docker.com/repository/docker/felipeaaa/library_api)

---

## 6. Observações Finais


* Sempre **faça login antes do build** se quiser facilitar o push depois.
* Sempre **use o nome do seu usuário no Docker Hub no `docker build`**.
* Evite `docker tag` depois de buildar com nome incorreto. Já nomeie certo desde o começo.
* Se precisar renomear, use:

  ```powershell
  docker tag imagem_local felipeaaa/library_api:1.0.0
  ```
