# Auditoría técnica inicial — APK de referencia

## Archivo inspeccionado
Duolingo v6.106.5 (Premium) (1).apk, aportado por el usuario.

## Observaciones verificadas mediante lectura del ZIP/APK
- 18.433 entradas internas.
- 149 archivos PNG.
- 294 archivos Rive (.riv).
- 301 archivos MP3.
- 11 archivos DEX.
- AndroidManifest.xml presente.
- Recursos de icono de inicio en res/mipmap-anydpi-v21/, incluyendo ic_launcher.xml y numerosas variantes.
- Bibliotecas nativas en lib/arm64-v8a/.

## Alcance y límites
La inspección fue del índice ZIP, **no** una descompilación funcional ni una validación de pantallas.
No se ha confirmado qué recursos pueden sustituirse sin romper la app.
El APK de referencia es software de terceros: no subir al repositorio ni redistribuir su código, audio o recursos propietarios.
No se ha generado una APK modificada de Duolingo ni probado el funcionamiento de su autenticación, progreso o servicios.

## Decisión técnica
Prioridad del usuario: mantener el flujo de aprendizaje, niveles, navegación y progreso, con una identidad gráfica propia.
Ruta A (evaluación): inventariar recursos reemplazables sin alterar la lógica; exigir pruebas reales antes de afirmar compatibilidad.
Ruta B (contingencia): implementar una aplicación independiente con experiencia de aprendizaje comparable y recursos originales.
