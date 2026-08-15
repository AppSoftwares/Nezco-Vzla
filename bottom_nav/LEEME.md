# Barra de navegación "manguera de incendio" — Grupo Nezco

Este paquete contiene los recursos reales para Android Studio (no una sola imagen).
Son los mismos archivos que usaría cualquier proyecto Android: fondo ilustrado +
5 íconos vectoriales-rasterizados + menú + selector de color, para que la barra
sea 100% funcional (clickeable, con estado activo/inactivo).

## Qué contiene

```
res/
 ├─ drawable-mdpi/    ┐
 ├─ drawable-hdpi/    │  bg_nav_manguera.png (el fondo con la manguera)
 ├─ drawable-xhdpi/   │  + los 5 íconos ic_nav_*.png
 ├─ drawable-xxhdpi/  │  en cada densidad de pantalla
 ├─ drawable-xxxhdpi/ ┘
 ├─ menu/
 │   └─ bottom_nav_menu.xml       (los 5 items del menú)
 ├─ color/
 │   ├─ nav_icon_tint.xml         (naranja si está activo, crema si no)
 │   └─ nav_text_tint.xml
 ├─ values/
 │   └─ colors_nezco_nav.xml      (colores de marca)
 └─ layout/
     └─ example_activity_main.xml (ejemplo de cómo combinarlo todo)
```

## Cómo instalarlo (3 pasos)

1. **Copia las carpetas** `drawable-mdpi`, `drawable-hdpi`, `drawable-xhdpi`,
   `drawable-xxhdpi`, `drawable-xxxhdpi`, `menu`, `color` dentro de
   `app/src/main/res/` de tu proyecto (fusiona con lo que ya exista, no
   reemplaces toda la carpeta `res`).

2. **Copia el contenido** de `values/colors_nezco_nav.xml` dentro de tu
   `res/values/colors.xml` (o deja el archivo tal cual, Android admite
   varios archivos de colores).

3. **En tu layout**, donde ya tengas el `BottomNavigationView`, agrégale
   estos atributos (mira `example_activity_main.xml` como referencia
   completa):

   ```xml
   <ImageView
       android:layout_width="match_parent"
       android:layout_height="72dp"
       android:scaleType="fitXY"
       android:src="@drawable/bg_nav_manguera" />

   <com.google.android.material.bottomnavigation.BottomNavigationView
       android:layout_width="match_parent"
       android:layout_height="72dp"
       android:background="@android:color/transparent"
       app:itemIconTint="@color/nav_icon_tint"
       app:itemTextColor="@color/nav_text_tint"
       app:menu="@menu/bottom_nav_menu" />
   ```

   La `ImageView` va detrás (el fondo con la manguera y los 5 aros de
   bronce vacíos) y el `BottomNavigationView` va encima, transparente,
   para que sus 5 íconos reales caigan justo dentro de los aros de bronce.

## Por qué así y no una sola imagen

Con una sola imagen "pintada" no puedes:
- Detectar el click en cada botón por separado.
- Mostrar el estado "seleccionado" (naranja) vs "no seleccionado" (crema).
- Reemplazar solo un ícono si cambias de opinión más adelante.

Separando fondo + íconos + menú, tienes una barra igual de vistosa pero
100% funcional con las herramientas normales de Android
(`BottomNavigationView` + `Menu` + `ColorStateList`).

## Si prefieres Jetpack Compose

Usa `bg_nav_manguera` como fondo de un `Box`, y dentro un `NavigationBar`
de Material 3 con `NavigationBarItem` por cada botón, aplicando
`NavigationBarItemDefaults.colors(selectedIconColor = NezcoOrange,
unselectedIconColor = NezcoCream)`. La lógica es la misma: fondo debajo,
barra transparente encima.
