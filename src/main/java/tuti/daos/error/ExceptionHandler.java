package tuti.daos.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import tuti.daos.excepciones.Excepcion;

@RestControllerAdvice
public class ExceptionHandler {

   @org.springframework.web.bind.annotation.ExceptionHandler(Excepcion.class)
   public ResponseEntity<ErrorAtributo> methodArgumentNotValidException(HttpServletRequest request, Excepcion e) {
	   return  ResponseEntity.status(e.getStatusCode()).body(new ErrorAtributo(e.getAtributo(),e.getMensaje()));
   }
}