$(async function () {
    await getTableWithUser();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    getCurrentUser: async () => await fetch('user/current'),
    findOneUser: async (id) => await fetch(`user/users/${id}`),
    findRoleById: async (id) => await fetch(`user/role/${id}`)
}

//строка для вывода в шапку страницы почты и ролей
let cur_email = '';
let cur_rolesStr = '';

async function currentUser() {
    let preuser = await userFetchService.getCurrentUser();
    let user = preuser.json();

    user.then(el => {
        cur_email = el.email;
        for (let i = 0; i < el.roles.length; i++) {
            cur_rolesStr += el.roles[i].role.replace('ROLE_', '') + ' ';
        }
        document.getElementById("user_Email").textContent = cur_email;
        document.getElementById("user_Roles").textContent = cur_rolesStr;
    })
}

currentUser();

async function getTableWithUser() {
    let table = $('#mainTableWithUser tbody');
    table.empty();

    await userFetchService.getCurrentUser()
        .then(res => res.json())
        .then(user => {
            console.log(user)
            let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.lastName}</td>                           
                            <td>${user.age}</td>    
                            <td>${user.email}</td>                                                         
                            <td>${user.authorities.map(el => el["role"].replace('ROLE_', ' '))}</td>                            
                        </tr>
                )`;
            table.append(tableFilling);
        })

}


