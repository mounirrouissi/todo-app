// apiHelpers.js
// apiHelpers.js

const BASE_URL = 'http://localhost:8080/api/v1';

const fetchWithErrorHandling = async (url, options = {}) => {
  try {
     const response = await fetch(url, options);
     if (!response.ok) {
       const errorData = await response.json(); // Assuming the backend sends JSON error data
       throw new Error(errorData.message || 'Network response was not ok');
     }
     return response.json();
  } catch (error) {
     console.error('Fetch error:', error.message);
     throw error;
  }
 };
   
export const editTodo = (objectFromFormData) => {

   return fetchWithErrorHandling(`${BASE_URL}/todos/${objectFromFormData.id}`, {
     method: 'PUT',
     headers: {
       'Content-Type': 'application/json'
     },
     body: JSON.stringify(objectFromFormData) 
   });
 }


export const  addToDo = (formData) => {
   const objectFromFormData = {};
   for (const pair of formData.entries()) {
       objectFromFormData[pair[0]] = pair[1];
   }
   

 
   return fetchWithErrorHandling(`${BASE_URL}/todos`, {
     method: 'POST',
     headers: {
       'Content-Type': 'application/json'
     },
     body: JSON.stringify(objectFromFormData) 
   });
 }
 


export const getTodos = async () => {
 return fetchWithErrorHandling(`${BASE_URL}/todos`);
};

export const deleteTodo = async (id) => {
 return fetchWithErrorHandling(`${BASE_URL}/todos/${id}`, {
    method: 'DELETE',
 });
};

// Add other API helper functions as needed

    // In apiHelpers.js
