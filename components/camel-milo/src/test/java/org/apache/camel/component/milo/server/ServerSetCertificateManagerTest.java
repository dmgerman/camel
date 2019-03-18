begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
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
name|milo
operator|.
name|AbstractMiloServerTest
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
comment|/**  * Test setting the certificate manager  */
end_comment

begin_class
DECL|class|ServerSetCertificateManagerTest
specifier|public
class|class
name|ServerSetCertificateManagerTest
extends|extends
name|AbstractMiloServerTest
block|{
annotation|@
name|Override
DECL|method|configureMiloServer (final MiloServerComponent server)
specifier|protected
name|void
name|configureMiloServer
parameter_list|(
specifier|final
name|MiloServerComponent
name|server
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|configureMiloServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
specifier|final
name|Path
name|baseDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/testing/cert/default"
argument_list|)
decl_stmt|;
specifier|final
name|Path
name|trusted
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"trusted"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|trusted
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/cert/certificate.der"
argument_list|)
argument_list|,
name|trusted
operator|.
name|resolve
argument_list|(
literal|"certificate.der"
argument_list|)
argument_list|,
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
name|server
operator|.
name|setServerCertificate
argument_list|(
name|loadDefaultTestKey
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|setDefaultCertificateValidator
argument_list|(
name|baseDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldStart ()
specifier|public
name|void
name|shouldStart
parameter_list|()
block|{     }
block|}
end_class

end_unit

