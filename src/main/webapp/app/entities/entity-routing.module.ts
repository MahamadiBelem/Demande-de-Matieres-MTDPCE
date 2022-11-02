import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'structure',
        data: { pageTitle: 'Structures' },
        loadChildren: () => import('./structure/structure.module').then(m => m.StructureModule),
      },
      {
        path: 'demande-matieres',
        data: { pageTitle: 'DemandeMatieres' },
        loadChildren: () => import('./demande-matieres/demande-matieres.module').then(m => m.DemandeMatieresModule),
      },
      {
        path: 'demande-reparations',
        data: { pageTitle: 'DemandeReparations' },
        loadChildren: () => import('./demande-reparations/demande-reparations.module').then(m => m.DemandeReparationsModule),
      },
      {
        path: 'type-matiere',
        data: { pageTitle: 'TypeMatieres' },
        loadChildren: () => import('./type-matiere/type-matiere.module').then(m => m.TypeMatiereModule),
      },
      {
        path: 'carnet-vehicule',
        data: { pageTitle: 'CarnetVehicules' },
        loadChildren: () => import('./carnet-vehicule/carnet-vehicule.module').then(m => m.CarnetVehiculeModule),
      },
      {
        path: 'marque-vehicule',
        data: { pageTitle: 'MarqueVehicules' },
        loadChildren: () => import('./marque-vehicule/marque-vehicule.module').then(m => m.MarqueVehiculeModule),
      },
      {
        path: 'revision-vehicule',
        data: { pageTitle: 'RevisionVehicules' },
        loadChildren: () => import('./revision-vehicule/revision-vehicule.module').then(m => m.RevisionVehiculeModule),
      },
      {
        path: 'matieres',
        data: { pageTitle: 'Matieres' },
        loadChildren: () => import('./matieres/matieres.module').then(m => m.MatieresModule),
      },
      {
        path: 'livraison-matieres',
        data: { pageTitle: 'LivraisonMatieres' },
        loadChildren: () => import('./livraison-matieres/livraison-matieres.module').then(m => m.LivraisonMatieresModule),
      },
      {
        path: 'type-panne',
        data: { pageTitle: 'TypePannes' },
        loadChildren: () => import('./type-panne/type-panne.module').then(m => m.TypePanneModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
