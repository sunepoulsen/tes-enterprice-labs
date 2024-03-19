export class I18NMocks {
  public static t(entries?: readonly (readonly [K: string, V: string])[]) {
    const store: Map<string, string> = new Map<string, string>(entries)

    return function (key: string): string | undefined {
      if (store.has(key)) {
        return store.get(key)
      }

      return 'unknown-key: ' + key
    }
  }

}
