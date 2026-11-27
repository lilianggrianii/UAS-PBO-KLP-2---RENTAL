package rental.model;

/**
 * Class abstrak untuk semua jenis akun (Admin dan Penyewa).
 * Menyimpan data login dengan field private (encapsulation).
 */
public abstract class Akun {

    private final String id;
    private final String username;
    private final String password;

    /**
     * @param id       ID unik akun
     * @param username username untuk login
     * @param password password untuk login
     */
    protected Akun(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Memeriksa kecocokan kredensial.
     *
     * @param username username yang dimasukkan
     * @param password password yang dimasukkan
     * @return true jika username dan password cocok
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * @return nama peran akun, misalnya "Admin" atau "Penyewa"
     */
    public abstract String getRole();

    /** @return ID akun */
    public String getId() {
        return id;
    }

    /** @return username akun */
    public String getUsername() {
        return username;
    }
}
