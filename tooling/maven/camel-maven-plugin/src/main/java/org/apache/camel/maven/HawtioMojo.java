begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * Runs a CamelContext using any Spring or Blueprint XML configuration files found in  *<code>META-INF/spring/*.xml</code>, and<code>OSGI-INF/blueprint/*.xml</code>,  * and<code>camel-*.xml</code> and starting up the context together with  *<a href="http://hawt.io/">hawtio</a> as web console.  *  * @goal hawtio  * @requiresDependencyResolution compile+runtime  * @execute phase="test-compile"  */
end_comment

begin_class
DECL|class|HawtioMojo
specifier|public
class|class
name|HawtioMojo
extends|extends
name|RunMojo
block|{
comment|/**      * The port number to use for the hawtio web console.      *      * @parameter property="camel.port"      *            default-value="8080"      */
DECL|field|port
specifier|private
name|int
name|port
init|=
literal|8080
decl_stmt|;
DECL|method|HawtioMojo ()
specifier|public
name|HawtioMojo
parameter_list|()
block|{
name|extendedPluginDependencyArtifactId
operator|=
literal|"hawtio-app"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeBootstrapCamel ()
specifier|protected
name|void
name|beforeBootstrapCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Starting hawtio ..."
argument_list|)
expr_stmt|;
name|Method
name|hawtioMain
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
literal|"io.hawt.app.App"
argument_list|)
operator|.
name|getMethod
argument_list|(
literal|"main"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|String
index|[]
operator|.
expr|class
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hawtioMain
operator|.
name|isAccessible
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Setting accessibility to true in order to invoke main()."
argument_list|)
expr_stmt|;
name|hawtioMain
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"--port"
block|,
literal|""
operator|+
name|port
block|,
literal|"--join"
block|,
literal|"false"
block|}
decl_stmt|;
name|hawtioMain
operator|.
name|invoke
argument_list|(
name|hawtioMain
argument_list|,
operator|new
name|Object
index|[]
block|{
name|args
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

