begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs.kerberos
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
operator|.
name|kerberos
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
operator|.
name|HdfsTestSupport
operator|.
name|CWD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
DECL|class|KerberosConfigurationBuilderTest
specifier|public
class|class
name|KerberosConfigurationBuilderTest
block|{
annotation|@
name|Test
DECL|method|withKerberosConfiguration ()
specifier|public
name|void
name|withKerberosConfiguration
parameter_list|()
block|{
comment|// given
name|String
name|kerberosConfigFileLocation
init|=
name|CWD
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/src/test/resources/kerberos/test-kerb5.conf"
decl_stmt|;
comment|// when
name|KerberosConfigurationBuilder
operator|.
name|setKerberosConfigFile
argument_list|(
name|kerberosConfigFileLocation
argument_list|)
expr_stmt|;
comment|// then
block|}
annotation|@
name|Test
DECL|method|setKerberosConfigFileWithRealFile ()
specifier|public
name|void
name|setKerberosConfigFileWithRealFile
parameter_list|()
block|{
comment|// given
name|String
name|kerb5FileName
init|=
literal|"test-kerb5.conf"
decl_stmt|;
name|String
name|kerberosConfigFileLocation
init|=
name|CWD
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/src/test/resources/kerberos/"
operator|+
name|kerb5FileName
decl_stmt|;
comment|// when
name|KerberosConfigurationBuilder
operator|.
name|setKerberosConfigFile
argument_list|(
name|kerberosConfigFileLocation
argument_list|)
expr_stmt|;
comment|// then
name|String
name|actual
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.security.krb5.conf"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actual
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|actual
operator|.
name|endsWith
argument_list|(
name|kerb5FileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setKerberosConfigFileWithMissingFile ()
specifier|public
name|void
name|setKerberosConfigFileWithMissingFile
parameter_list|()
block|{
comment|// given
name|String
name|kerb5FileName
init|=
literal|"missing-kerb5.conf"
decl_stmt|;
name|String
name|kerberosConfigFileLocation
init|=
name|CWD
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/src/test/resources/kerberos/"
operator|+
name|kerb5FileName
decl_stmt|;
comment|// when
name|KerberosConfigurationBuilder
operator|.
name|setKerberosConfigFile
argument_list|(
name|kerberosConfigFileLocation
argument_list|)
expr_stmt|;
comment|// then
name|String
name|actual
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.security.krb5.conf"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

