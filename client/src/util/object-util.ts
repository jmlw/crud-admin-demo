export const get = (obj: object, path: string, defaultValue: any): any => {
  const result = String.prototype.split
    .call(path, /[,[\].]+?/)
    .filter(Boolean)
    .reduce((res: any, key: string) => {
      return res !== null && res !== undefined ? res[key] : res;
    }, obj);
  return result === undefined || result === obj ? defaultValue : result;
};
