```mermaid

sequenceDiagram

    actor Bruker
    participant App
    participant API

    Bruker ->> App: Click on retrive weather app
    App ->> API: fetchWeatcherButton()

    alt suksess
        API -->> App: Weather is returned
        App -->> Bruker: Show weather data

    else feil
        API -->> App: Error
        App -->> Bruker: Show error message

    end 




``````