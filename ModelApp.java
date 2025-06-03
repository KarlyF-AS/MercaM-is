public class ModelApp {
    public static void main(String[] args) {
        Usuario usuario = Model.validarLogin("danifv02@gmail.com", "12345678");
        System.out.printf(String.valueOf(usuario));
    }

}
