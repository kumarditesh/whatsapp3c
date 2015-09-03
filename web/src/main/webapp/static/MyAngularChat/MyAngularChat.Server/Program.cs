using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using Spike;

namespace MyAngularChat
{
    class Program
    {
        static void Main(string[] args)
        {

            // Start listening on the port 8002
            Service.Listen(
                new TcpBinding(IPAddress.Any, 8002)
                );
        }
    }
}
