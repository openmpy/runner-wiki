export async function getLatestDocuments(
  category: string,
  page: number = 0,
  size: number = 15
) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents?category=${category}&page=${page}&size=${size}`,
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
