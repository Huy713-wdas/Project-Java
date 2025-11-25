import React, { useState } from 'react';
import api from '../api';


export default function UploadJourney({ ownerId, onDone }) {
const [file, setFile] = useState(null);
const upload = async () => {
const fd = new FormData();
fd.append('file', file);
fd.append('ownerId', ownerId);
const res = await api.post('/journeys/upload-csv', fd);
onDone(res.data);
}
return (
<div>
<input type="file" accept=".csv" onChange={e => setFile(e.target.files[0])} />
<button onClick={upload} disabled={!file}>Upload</button>
</div>
);
}