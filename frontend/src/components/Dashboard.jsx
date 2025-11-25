import React, { useEffect, useState } from 'react';
import api from '../api';


export default function Dashboard({ ownerId }){
const [journeys, setJourneys] = useState([]);
useEffect(()=>{ api.get(`/journeys?ownerId=${ownerId}`).then(r=>setJourneys(r.data)); },[]);
const totalSavedKg = journeys.reduce((s,j)=>s + (j.savedKgCO2||0),0);
const totalCredits = journeys.reduce((s,j)=>s + (j.credits||0),0);
return (
<div>
<h2>Dashboard</h2>
<p>Total saved CO2: {totalSavedKg.toFixed(2)} kg</p>
<p>Total credits: {totalCredits.toFixed(4)} tons</p>
</div>
);
}