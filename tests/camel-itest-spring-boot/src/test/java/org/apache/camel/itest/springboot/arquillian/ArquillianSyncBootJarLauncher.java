begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.arquillian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|arquillian
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|loader
operator|.
name|JarLauncher
import|;
end_import

begin_comment
comment|/**  * A Spring-boot jar launcher that uses the current thread instead of creating a new thread for spring-boot.  */
end_comment

begin_class
DECL|class|ArquillianSyncBootJarLauncher
specifier|public
class|class
name|ArquillianSyncBootJarLauncher
extends|extends
name|JarLauncher
block|{
DECL|field|classLoader
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|method|ArquillianSyncBootJarLauncher ()
specifier|public
name|ArquillianSyncBootJarLauncher
parameter_list|()
block|{     }
DECL|method|run (String[] args)
specifier|public
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|launch
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|launch (String[] args, String mainClass, ClassLoader classLoader)
specifier|protected
name|void
name|launch
parameter_list|(
name|String
index|[]
name|args
parameter_list|,
name|String
name|mainClass
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
name|Runnable
name|runner
init|=
name|createMainMethodRunner
argument_list|(
name|mainClass
argument_list|,
name|args
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|runner
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the classloader used by spring, to communicate with it.      *      * @return the spring classloader      */
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
block|}
end_class

end_unit

