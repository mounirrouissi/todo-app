import React, { useContext, useEffect, useRef, useState } from 'react'
import { useQuery } from 'react-query';
import { useMutation, useQueryClient } from 'react-query';

import { FaPlus, FaTrashAlt } from 'react-icons/fa'; // Delete icon
import { FaEdit } from 'react-icons/fa'; // Edit icon
import { addToDo, deleteTodo, editTodo } from '../api/apiHelpers';
import { ThemeContext } from '../context/ThemeContext';

import { toast } from 'react-toastify';

const ToDo = ({ todo, handleEditClick,handleDelete }) => {
    const { isDarkMode } = useContext(ThemeContext);

 

    // Determine the background color based on isDarkMode
    const bgColor = isDarkMode ? 'bg-gray-800' : 'bg-white';
    const textColor = isDarkMode ? 'text-white' : 'text-gray-800';

    return (
        <li className={`flex items-center justify-between gap-4 p-4 border-b border-gray-200 rounded-md shadow-sm ${bgColor} ${textColor}`}> 
          <div className="flex items-center gap-2">
            <span className={todo.isCompleted ? 'line-through text-gray-500' : ''}>
              {todo.description} 
            </span>
            <span className={`badge ${todo.isCompleted ? 'bg-green-500' : 'bg-yellow-500'}`}>
              {todo.isCompleted ? 'Completed' : 'Incomplete'}
            </span>
          </div>
    
          <div className="flex items-center gap-2"> 
            <button className={`btn btn-sm btn-primary ${isDarkMode ? 'dark:bg-blue-700' : ''}`} onClick={() => handleEditClick(todo.id)}> 
                <FaEdit />
            </button>
            <button className={`btn btn-sm btn-error ${isDarkMode ? 'dark:bg-red-700' : ''}`} onClick={() => handleDelete(todo.id)}>
                <FaTrashAlt />
            </button>
          </div>
        </li>
    );
};


function Hero() {

    // ... other state variables and imports
    const { isDarkMode } = useContext(ThemeContext);

    // State to store the ID of the todo item being edited
    const [editingTodoId, setEditingTodoId] = useState(null);
    const [newToDoDescription, setNewToDoDescription] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [isCompleted, setIsCompleted] = useState(false);

    const descriptionRef = useRef(null);
    const isCompletedRef = useRef(null);

    const [isEditFormOpen, setIsEditFormOpen] = useState(false);

    // ... other code ...

    const queryClient = useQueryClient();

    const handleDescriptionChange = (event) => {
        setNewToDoDescription(event.target.value);
    };
    
    const handleCompletionChange = (event) => {
        setIsCompleted(event.target.checked);
    };


    const deleteTodoMutation = useMutation((id) => deleteTodo(id), {
        onSuccess: () => {
          queryClient.invalidateQueries(); 
          toast.success("Todo deleted successfully");
        }
      });
      
      const handleDelete = (id) => {
        deleteTodoMutation.mutate(id);
      };
      
    const addMutation = useMutation((formData) => addToDo(formData), {
        onSuccess: () => {
            // Invalidate and refetch the 'todoslist' query after a successful deletion
            queryClient.invalidateQueries('todoslist');
            toast.success("Todo added successfully");

        },
    });

    const editMutation=useMutation((objectFromFormData)=>editTodo(objectFromFormData),{
        onSuccess: () => {
            // Invalidate and refetch the 'todoslist' query after a successful deletion
            queryClient.invalidateQueries('todoslist');
            toast.success("Todo edited successfully");
        },
        onError: (error) => {
            console.error('Error editing todo:', error);
            setErrorMessage(error);
            toast.error(error.message);

        },

    });

    const { isLoading, error, data: todos } = useQuery(
        'todoslist', 
        async () => {
        const response = await fetch('http://ec2-18-119-162-141.us-east-2.compute.amazonaws.com:8085/api/v1/todos'); // Your API endpoint
        return response.json();
    });

    // ... other code




    const handleEditClick = async (id) => {
        console.log("handleEdit clicked with id: " + id);
        setEditingTodoId(id);
        setIsEditFormOpen(!isEditFormOpen);

    }
  
   

    const handleSubmit = async (event, todoId) => {
        event.preventDefault(); // Prevent default form submission
    
        console.log("Submitting form for todo ID:", todoId);
    
        const formData = new FormData();
        formData.append('description', newToDoDescription); 
        formData.append('isCompleted', isCompleted);
    
        console.log("handle Submit");
        const objectFromFormData = { id: todoId };
        for (const pair of formData.entries()) {
            objectFromFormData[pair[0]] = pair[1];
        }
        
        console.log("objectFromFormData:", objectFromFormData);
    
        try {
            if (todoId) {
                // If todoId is provided, it's an edit operation
                // You might need to adjust this part based on how your backend handles edits
                // For example, you might need to call a different mutation function for editing
                await editMutation.mutateAsync(objectFromFormData).then(setIsEditFormOpen(false));
            } else {
                // If todoId is not provided, it's a new todo operation
                await addMutation.mutateAsync(formData);
            }
            setNewToDoDescription(''); // Clear input field
            setIsOpen(false); // Close the form
        } catch (error) {
            // Handle errors, display messages, etc.
        }
    };

useEffect(()=>{
console.log("error message:",errorMessage.message)
},[errorMessage])

        return (
            <div className="container w-full mx-auto p-4">
                <div>
                    {isLoading ? (
                        <p className='w-full text-9xl'>Loading Todos...</p>

                    ) : todos.length === 0 ? (
                    <p className='text-lg font-bold'>No Todos available. Click the + button to add one.</p>
                    )
                    
                    : error ? (
                        <p>Error: {error.message}</p>
                    ) : (
                        <ul className='text-4xl'>
                            {
                                todos.map((todo) => (
                                <ToDo todo={todo} isEditFormOpen={isEditFormOpen} handleEditClick={handleEditClick} handleDelete={handleDelete} />
                            ))}
                        </ul>
                    )}
                </div>
                
                {isOpen &&
                    <dialogue open className="mb-4 h-auto fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white rounded-md shadow-xl z-50 p-8 max-w-md w-full">
                        <form onSubmit={handleSubmit} className="space-y-6">
                            <div className="flex flex-col space-y-4">
                                <input
                                    type="text"
                                    placeholder="Enter new task"
                                    ref={descriptionRef} // Assign a ref
                                    value={newToDoDescription}
                                    onChange={handleDescriptionChange}
                                    className="border px-4 py-2 text-lg rounded-lg shadow-sm"
                                />
                                <label className="flex items-center gap-2">
                                    <input
                                        type="checkbox"
                                        ref={isCompletedRef} // Assign a ref
                                        checked={isCompleted} // Assuming isCompleted is a state variable to track completion
                                        onChange={handleCompletionChange} // Assuming handleCompletionChange is a function to update isCompleted
                                        className="border p-2 h-5 w-5"
                                    />
                                    <span>Completed</span>
                                </label>
                            </div>
                            <div className="flex justify-center">
                                <button type="submit" className="px-4 py-2 rounded-md bg-green-500 text-white shadow">
                                    Add Task
                                </button>
                            </div>
                        </form>
                        <button type="button" className="absolute top-0 right-0 border p-2 rounded-full bg-red-500 text-white" onClick={() => setIsOpen(false)}>
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </dialogue>
                }


                {isEditFormOpen && (
                    <dialogue open className="mb-4 h-1/8 p-10 fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white p-5 rounded-md shadow-xl z-50" >

                <form onSubmit={handleSubmit}> 
                    <input type="text" placeholder={todos.find(todo =>todo.id === editingTodoId).description} onChange={handleDescriptionChange} />
                    <button onClick={(event) => handleSubmit(event, editio.id === editingTodoId).description} onChange={handleDescriptionChange} />
                    <button onClick={(event) => handleSubmit(event, editingTodoId)} className="border p-1 rounded-full bg-green-500 text-white shadow-lg">

                    Save</button>
                </form>
                </dialogue>
            )}


                <button className="fixed right-20 bottom-20 bg-green-500 hover:bg-green-600  text-white  rounded-full  text-4xl p-2" onClick={() => setIsOpen(!isOpen)}>
                    <FaPlus/>
                </button>
            </div>
        );
    }



export default Hero