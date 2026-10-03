function fn() {
  // Every value can be overridden with -Ddemoblaze.<name>=... or a DEMOBLAZE_<NAME> environment variable.
  // Defaults point at the public Demoblaze demo, which needs no secrets.
  function setting(name, fallback) {
    var envName = 'DEMOBLAZE_' + name.replace(/([A-Z])/g, '_$1').toUpperCase();
    return karate.sysprop('demoblaze.' + name) || karate.sysenv(envName) || fallback;
  }

  var config = {
    baseUrl: setting('apiUrl', 'https://api.demoblaze.com'),
    passwordPrefix: setting('passwordPrefix', 'Qa-')
  };

  // The public demo is occasionally slow; fail fast instead of hanging the build.
  karate.configure('connectTimeout', 10000);
  karate.configure('readTimeout', 15000);
  karate.configure('retry', { count: 2, interval: 2000 });
  return config;
}
