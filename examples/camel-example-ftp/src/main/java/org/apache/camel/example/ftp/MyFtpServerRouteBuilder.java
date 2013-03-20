begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.ftp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|ftp
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
import|;
end_import

begin_comment
comment|/**  * Server route  */
end_comment

begin_class
DECL|class|MyFtpServerRouteBuilder
specifier|public
class|class
name|MyFtpServerRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure properties component
name|PropertiesComponent
name|pc
init|=
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"properties"
argument_list|,
name|PropertiesComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|pc
operator|.
name|setLocation
argument_list|(
literal|"classpath:ftp.properties"
argument_list|)
expr_stmt|;
comment|// lets shutdown faster in case of in-flight messages stack up
name|getContext
argument_list|()
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"{{ftp.server}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/download"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Downloaded file ${file:name} complete."
argument_list|)
expr_stmt|;
comment|// use system out so it stand out
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"*********************************************************************************"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Camel will route files from the FTP server: "
operator|+
name|getContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{ftp.server}}"
argument_list|)
operator|+
literal|" to the target/download directory."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You can configure the location of the ftp server in the src/main/resources/ftp.properties file."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Use ctrl + c to stop this application."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"*********************************************************************************"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

