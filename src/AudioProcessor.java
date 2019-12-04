public class AudioProcessor {
    private UserInterface userInterface;     // Ссылка на пользовательский интерфейс
    private AudioOutput audioOutput = new AudioOutput();

    public AudioProcessor() {
        this.userInterface = new UserInterface(this);   // Передаем текущий экземпляр нашего синтезатора
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public AudioOutput getAudioOutput(){
        return audioOutput;
    }
}
