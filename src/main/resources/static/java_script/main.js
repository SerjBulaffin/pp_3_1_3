$(async function () {
    await getTableWithUsers();
    getNewUserForm();
    getDefaultModal();
    addNewUser();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('api/users'),
    getCurrentUser: async () => await fetch('api/current'),
    findOneUser: async (id) => await fetch(`api/users/${id}`),
    addNewUser: async (user) => await fetch('api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`api/users/${id}`, {
        method: 'PUT',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`api/users/${id}`, {method: 'DELETE', headers: userFetchService.head}),
    findRoleById: async (id) => await fetch(`api/role/${id}`)
}

//строка для вывода в шапку страницы почты и ролей
let cur_email = '';
let cur_rolesStr = '';

async function currentUser() {
    let preuser = await userFetchService.getCurrentUser();
    let user =  preuser.json();

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

async function getTableWithUsers() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.lastName}</td>                           
                            <td>${user.age}</td>    
                            <td>${user.email}</td>                                                         
                            <td>${user.authorities.map(el => el["role"].replace('ROLE_', ' '))}</td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-info" 
                                data-toggle="modal" data-target="#someDefaultModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#someDefaultModal">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })

    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#mainTableWithUsers").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}


async function getNewUserForm() {
    let button = $(`#SliderNewUserForm`);
    let form = $(`#defaultSomeForm`)
    button.on('click', () => {
        if (form.attr("data-hidden") === "true") {
            form.attr('data-hidden', 'false');
            form.show();
            button.text('Hide panel');
        } else {
            form.attr('data-hidden', 'true');
            form.hide();
            button.text('Show panel');
        }
    })
}


// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">  
            <div class="mb-3 text-center">
                <label for="inputIdEdit">ID</label>
                <input type="text" readonly value="${user.id}" name="id "class="form-control"
                 id="inputIdEdit">
            </div>
            
            <div class="mb-3 text-center">
                 <label for="inputFirstNameEdit">First name</label>
                 <input type="text" value="${user.username}" name="firstName" class="form-control"
                 id="inputFirstNameEdit">
            </div>
            
            <div class="mb-3 text-center">
                 <label for="inputLastNameEdit">Last name</label>
                 <input type="text" value="${user.lastName}" name="lastName" class="form-control"
                 id="inputLastNameEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputAgeEdit">Age</label>
                  <input type="number" value="${user.age}" name="age" class="form-control"
                  id="inputAgeEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputEmailEdit">Email</label>
                  <input type="text" value="${user.email}" name="email" class="form-control"
                  id="inputEmailEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputPassEdit"><b>Password</b></label>
                  <input id="inputPassEdit" class="form-control" type="text"
                  name="password">
            </div>
            
            <div class="mb-3 text-center">
                    <label for="role_selectU"><b>Role</b></label>
                    <select class="form-select" name="role_select" id="role_selectEdit" size="2" multiple>                          
                            <option value="1" name="ROLE_ADMIN">ROLE_ADMIN</option>
                            <option value="2" name="ROLE_USER">ROLE_USER</option>
                    </select>
            </div>                
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })


    $("#editButton").on('click', async () => {
            let id = modal.find("#inputIdEdit").val().trim();
            let firstName = modal.find("#inputFirstNameEdit").val().trim();
            let lastName = modal.find("#inputLastNameEdit").val().trim();
            let age = modal.find("#inputAgeEdit").val().trim();
            let email = modal.find("#inputEmailEdit").val().trim();
            let password = modal.find("#inputPassEdit").val().trim();
            let roleSelect = modal.find("#role_selectEdit").val(); //получаю ID ролей из модалки

            let rolesObj = []; //временный массив для хранения ролей

            //итерируем отмеченные роли и по их ID вытягиваем из базы, создаем новый объект и ложим в массив
            for (let i = 0; i < roleSelect.length; i++) {
                await userFetchService.findRoleById(roleSelect[i]).then(resp => resp.json().then(el => {
                    var ri = {id: el['id'], role: el['role']};
                    rolesObj.push(ri)
                }))
            }
            let data = {
                id: id,
                firstName: firstName,
                lastName: lastName,
                age: age,
                email: email,
                password: password,
                roles: rolesObj
            }

            //запрос на сохранение в БД
            const response = await userFetchService.updateUser(data, id);

            //при удачном сохранении перерисовываем таблицу юзеров
            if (response.ok) {
                getTableWithUsers();
                modal.modal('hide'); //убираем модалку
            } else {
                let body = await response.json();
                let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
                modal.find('.modal-body').prepend(alert);
            }
        }
    )
}


// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Delete user');

    let deletButton = `<button  class="btn btn-outline-success" id="deleteButton">Delete</button>`;
    let closeMButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deletButton);
    modal.find('.modal-footer').append(closeMButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">  
            <div class="mb-3 text-center">
                <label for="inputIdEdit">ID</label>
                <input type="text" readonly value="${user.id}" name="id "class="form-control"
                 id="inputIdEdit">
            </div>
            
            <div class="mb-3 text-center">
                 <label for="inputFirstNameEdit">First name</label>
                 <input type="text" readonly value="${user.username}" name="firstName" class="form-control"
                 id="inputFirstNameEdit">
            </div>
            
            <div class="mb-3 text-center">
                 <label for="inputLastNameEdit">Last name</label>
                 <input type="text" readonly value="${user.lastName}" name="lastName" class="form-control"
                 id="inputLastNameEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputAgeEdit">Age</label>
                  <input type="number" value="${user.age}" readonly name="age" class="form-control"
                  id="inputAgeEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputEmailEdit">Email</label>
                  <input type="text" readonly value="${user.email}" name="email" class="form-control"
                  id="inputEmailEdit">
            </div>

            <div class="mb-3 text-center">
                  <label for="inputPassEdit"><b>Password</b></label>
                  <input id="inputPassEdit" class="form-control" type="text" readonly name="password">
            </div>
            
            <div class="mb-3 text-center">
                    <label for="role_selectU"><b>Role</b></label>
                    <select class="form-select" name="role_select" id="role_selectEdit" size="2" multiple>                          
                            <option value="1" name="ROLE_ADMIN">ROLE_ADMIN</option>
                            <option value="2" name="ROLE_USER">ROLE_USER</option>
                    </select>
            </div>                
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })


    $("#deleteButton").on('click', async () => {
        await userFetchService.deleteUser(id);
        getTableWithUsers();
        modal.modal('hide');
    })

}

//Добавление нового пользователя
async function addNewUser() {
    $('#addNewUserButton').click(async () => {
        //let addUserForm = $('#defaultSomeForm')
        let addUserForm = $('#defaultSomeForm')
        let firstName = addUserForm.find('#first_name').val().trim();
        let lastName = addUserForm.find('#last_name').val().trim();
        let age = addUserForm.find('#age').val().trim();
        let email = addUserForm.find('#email').val().trim();
        let password = addUserForm.find('#password').val().trim();
        let roleSelect = addUserForm.find("#role_select").val(); //получаю ID ролей из модалки

        let rolesObj = []; //временный массив для хранения ролей

        //итерируем отмеченные роли и по их ID вытягиваем из базы, создаем новый объект и ложим в массив
        for (let i = 0; i < roleSelect.length; i++) {
            await userFetchService.findRoleById(roleSelect[i]).then(resp => resp.json().then(el => {
                var ri = {id: el['id'], role: el['role']};
                rolesObj.push(ri)
            }))
        }

        let data = {
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: rolesObj
        }

        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            $('.modal').modal('hide'); //закрытие внешнего модального окна
            getTableWithUsers();
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}

