begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rmi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rmi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Producer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The rmi component is for invoking Java RMI beans from Camel.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"rmi"
argument_list|,
name|title
operator|=
literal|"RMI"
argument_list|,
name|syntax
operator|=
literal|"rmi:hostname:port/name"
argument_list|,
name|consumerClass
operator|=
name|RmiConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|RmiEndpoint
specifier|public
class|class
name|RmiEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|classLoader
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Hostname of RMI server"
argument_list|,
name|defaultValue
operator|=
literal|"localhost"
argument_list|)
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Port number of RMI server"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|Registry
operator|.
name|REGISTRY_PORT
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name to use when binding to RMI server"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|remoteInterfaces
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|remoteInterfaces
decl_stmt|;
annotation|@
name|UriParam
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|method|RmiEndpoint ()
specifier|public
name|RmiEndpoint
parameter_list|()
block|{     }
DECL|method|RmiEndpoint (String endpointUri, RmiComponent component)
specifier|protected
name|RmiEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RmiComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|RmiEndpoint (String endpointUri)
specifier|public
name|RmiEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
if|if
condition|(
name|remoteInterfaces
operator|==
literal|null
operator|||
name|remoteInterfaces
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"To create a RMI consumer, the RMI endpoint's remoteInterfaces property must be be configured."
argument_list|)
throw|;
block|}
name|RmiConsumer
name|answer
init|=
operator|new
name|RmiConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
return|return
operator|new
name|RmiProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
expr_stmt|;
block|}
comment|// skip leading slash
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
return|return
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
name|path
return|;
block|}
DECL|method|getRegistry ()
specifier|public
name|Registry
name|getRegistry
parameter_list|()
throws|throws
name|RemoteException
block|{
if|if
condition|(
name|uri
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|uri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|LocateRegistry
operator|.
name|getRegistry
argument_list|()
return|;
block|}
block|}
DECL|method|getRemoteInterfaces ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getRemoteInterfaces
parameter_list|()
block|{
return|return
name|remoteInterfaces
return|;
block|}
comment|/**      * To specific the remote interfaces.      */
DECL|method|setRemoteInterfaces (List<Class<?>> remoteInterfaces)
specifier|public
name|void
name|setRemoteInterfaces
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|remoteInterfaces
parameter_list|)
block|{
name|this
operator|.
name|remoteInterfaces
operator|=
name|remoteInterfaces
expr_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
operator|&&
operator|!
name|remoteInterfaces
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|classLoader
operator|=
name|remoteInterfaces
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setRemoteInterfaces (Class<?>.... remoteInterfaces)
specifier|public
name|void
name|setRemoteInterfaces
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|remoteInterfaces
parameter_list|)
block|{
name|setRemoteInterfaces
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|remoteInterfaces
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getClassLoader ()
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|classLoader
return|;
block|}
DECL|method|setClassLoader (ClassLoader classLoader)
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/**      * You can set the name of the method to invoke.      */
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
block|}
end_class

end_unit

