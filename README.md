

   E_Library web application backend using the Spring Boot framework.


DB design and CSV Parser
      -	  Uploading and parsing provided CSV files
      -	  Created a corresponding DB tables design to store data from CSV files
      -	  Made corrections and error handling for parsed data
      -	  Storing data into DB in best-optimized way
Image files download
      -	  Downloading images using scheduled jobs with multiple threads
      -	  Jobs are designed to handle parallel download
      -	  Conflicts handled between two jobs
      -	  Provided Controller API endpoint to download images from server
Search and filtering
      -	  For all data stored in DB, there are filtering functionality
      -	  API endpoints expect query params to filter
      -	  All GET collection queries support pagination
Roles and Authentication
      -	  Used Spring Security framework for authentication
      -	  Used JWT based authentication
      -	  Implement 3 roles in system (admin, user, employee)
