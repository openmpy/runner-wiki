export async function getLatestDocuments(category: string) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents?category=${category}&page=0&size=15`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-cache",
      }
    );

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}
