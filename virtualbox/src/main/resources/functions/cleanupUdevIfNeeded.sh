function cleanupUdevIfNeeded {
   if [ -f '/etc/udev/rules.d/70-persistent-net.rules' ]
   then
      rm /etc/udev/rules.d/70-persistent-net.rules
      mkdir /etc/udev/rules.d/70-persistent-net.rules
      rm -rf /dev/.udev/
      rm /lib/udev/rules.d/75-persistent-net-generator.rules
   fi
}