package model;

public class Usuario {
	private Integer id;
	private String nombre;
	private String apellidos;
	private String usuario;
	private String contrasena;
	private String pais;
	private String tecnologia;

	public Usuario(Integer id, String nombre, 
			String apellidos, String usuario, String contrasena,
			String pais, String tecnologia) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.pais = pais;
		this.tecnologia = tecnologia;
	}
	
	public Usuario(String nombre, String apellidos,
			String usuario, String contrasena, 
			String pais, String tecnologia) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.pais = pais;
		this.tecnologia = tecnologia;
	}
	public boolean isValid() {
		return true;
	}
	//getters
	public String getTecnologia() {
		return this.tecnologia;
	}

	public String getPais() {
		return this.pais;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public String getNombre() {
		return this.nombre;
	}

	public Integer getId() {
		return this.id;
	}

	//setters
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
