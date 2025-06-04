public class Controller2 {

    public boolean filtroPassword(String password) {
        if (password == null || password.length() < 8) return false;

        int letras = 0, digitos = 0;

        for (char k : password.toCharArray()) {
            if (Character.isLetter(k)) letras++;
            else if (Character.isDigit(k)) digitos++;
        }
        return letras >= 4 && digitos >= 2;
    }

    public boolean validarEmail(String email) {
        if (email == null) return false;

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
