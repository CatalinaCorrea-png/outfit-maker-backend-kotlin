package ar.outfitmaker.errors

abstract class AppException(val code: String, msg: String) : RuntimeException(msg)

// La petición no pudo procesarse porque contiene datos inválidos o no cumple
// con las reglas de negocio definidas.
// (e.g., intentar transferir un monto negativo, un campo obligatorio vacío,
//         o un valor fuera de rango permitido).
class BusinessException(code: String, msg: String) : AppException(code, msg)

// El recurso solicitado no fue encontrado en el sistema.
// (e.g., buscar un usuario por ID que no existe en la base de datos,
//        acceder a un producto que fue eliminado, o consultar una orden inexistente).
class NotFoundException(code: String, msg: String) : AppException(code, msg)

// La petición no pudo completarse debido a un conflicto con el estado actual del recurso.
// (e.g., intentar crear un registro que ya existe, violando una restricción de unicidad).
class ConflictException(code: String, msg: String) : AppException(code, msg)

// Ocurrió un error inesperado e interno en el servidor que no está relacionado
// con la entrada del usuario.
// (e.g., fallo de conexión a la base de datos, un servicio externo no disponible,
//         o un error no controlado en la lógica del sistema).
class InternalException(code: String, msg: String) : AppException(code, msg)

// Token expirado. (401)
class TokenExpiredException(msg: String = "Token vencido") : AppException("TOKEN_EXPIRED", msg)