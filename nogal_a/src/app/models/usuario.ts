export interface Usuario {
    id?: number;
    username: string;
    password: string;
    nombres: string;
    apellidos: string;
    email: string;
    telefono: string;
    tipoDocumento: string; // "DNI" o "CE"
    numeroDocumento: string;
    rol?: string; // Opcional porque en registro se asigna automáticamente como "cliente"
}