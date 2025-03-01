package org.educastur.samuelepv59;

import java.util.Comparator;

public class ComparaArtPorPrecio implements Comparator<Articulo> {
    @Override
    public int compare(Articulo a1, Articulo a2) {
        return Double.compare(a1.getPvp(),a2.getPvp());
    }
}
