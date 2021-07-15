shell = ${SHELL}
.PHONY: all clean test default hook unhook

default:
	echo 'Makefile default target!'

dev-tools:
	$(info ******** Installing ASDF ********)
	@cd scripts || exit 1 && chmod +x asdf_install.sh && ./asdf_install.sh
	$(info ******** Installing Concourse Fly CLI ********)
	@cd scripts || exit 1 && chmod +x fly_install.sh && ./fly_install.sh

hook:
	# TODO: DESIGN THE GITHOOK
	#git config core.hooksPath .githooks
unhook:
	git config --unset core.hooksPath

install_asdf:
	cd scripts || exit 1 && chmod +x asdf_install.sh && ./asdf_install.sh

sphinx_update:
	cd docs || exit 1 && make html

jarvis_dev_docker:
	$(info ******** Installing JARVIS Docker Environment ********)
	@cd build_scripts/dev || exit 1 && ./build_jarvis_docker.sh

jarvis_dev_local:
	$(info ******** Installing JARVIS Local Environment ********)
	@make install
	@make super_user
	@make start_server

start_server:
	$(info ******** Installing Python Environment and Starting Django Services ********)
	@dev/local/.python/bin/python jarvis/manage.py runserver

install:
	$(info ******** Installing Python Environment ********)
	@ ./scripts/pre-configure.sh
	@./scripts/setup.sh -c
	@./scripts/setup.sh -i

super_user:
	$(info ******** Create the SuperUser Account ********)
	@./scripts/setup.sh -s

clean:
	$(info ******** Removing Python Environments ********)
	@./scripts/setup.sh -c

	$(info ******** Removing Local Database Backups ********)
	@if [ -d database_backups ]; then rm -rf database_backups; fi
