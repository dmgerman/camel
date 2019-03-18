begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.util
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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|itest
operator|.
name|springboot
operator|.
name|ITestConfigBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|impl
operator|.
name|base
operator|.
name|exporter
operator|.
name|zip
operator|.
name|ZipExporterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Utility to export a spring-boot jar and check the content.  */
end_comment

begin_class
DECL|class|JarExporter
specifier|public
class|class
name|JarExporter
block|{
annotation|@
name|Test
DECL|method|exportJar ()
specifier|public
name|void
name|exportJar
parameter_list|()
throws|throws
name|Exception
block|{
name|Archive
argument_list|<
name|?
argument_list|>
name|archive
init|=
name|ArquillianPackager
operator|.
name|springBootPackage
argument_list|(
operator|new
name|ITestConfigBuilder
argument_list|()
operator|.
name|module
argument_list|(
literal|"camel-websocket"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
operator|new
name|ZipExporterImpl
argument_list|(
name|archive
argument_list|)
operator|.
name|exportTo
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/export.zip"
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

