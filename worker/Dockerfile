FROM microsoft/dotnet:2.0.0-sdk

WORKDIR /code

ADD src/Worker /code/src/Worker

RUN dotnet restore -v minimal src/Worker \
    && dotnet publish -c Release -o "./" "src/Worker/" 

ENV PORT = 8080

EXPOSE 8080

CMD dotnet src/Worker/Worker.dll