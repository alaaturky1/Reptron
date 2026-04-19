import { useEffect, useState } from "react";
import { getProducts } from "../services/storeService.js";
import { getApiErrorMessage } from "../api/responseUtils.js";

/**
 * Product list with loading/error; pass optional category/search when filtering via API.
 */
export function useProducts(params = {}) {
  const { category, search } = params;
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);
    getProducts({ category, search })
      .then((rows) => {
        if (!cancelled) setData(rows);
      })
      .catch((e) => {
        if (!cancelled) setError(getApiErrorMessage(e));
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [category, search]);

  return { data, loading, error };
}
