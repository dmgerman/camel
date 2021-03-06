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
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
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

begin_class
DECL|class|KerberosAuthenticationTest
specifier|public
class|class
name|KerberosAuthenticationTest
block|{
DECL|field|underTest
specifier|private
name|KerberosAuthentication
name|underTest
decl_stmt|;
annotation|@
name|Test
DECL|method|loginWithKeytabFile ()
specifier|public
name|void
name|loginWithKeytabFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|String
name|username
init|=
literal|"test_user"
decl_stmt|;
name|String
name|keyTabFileLocation
init|=
name|CWD
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/src/test/resources/kerberos/test-keytab.bin"
decl_stmt|;
name|underTest
operator|=
operator|new
name|KerberosAuthentication
argument_list|(
name|configuration
argument_list|,
name|username
argument_list|,
name|keyTabFileLocation
argument_list|)
expr_stmt|;
comment|// when
name|underTest
operator|.
name|loginWithKeytab
argument_list|()
expr_stmt|;
comment|// then
comment|/* message is printed in the logs */
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FileNotFoundException
operator|.
name|class
argument_list|)
DECL|method|loginWithMissingKeytabFile ()
specifier|public
name|void
name|loginWithMissingKeytabFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|String
name|username
init|=
literal|"test_user"
decl_stmt|;
name|String
name|keyTabFileLocation
init|=
name|CWD
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/src/test/resources/kerberos/missing.bin"
decl_stmt|;
name|underTest
operator|=
operator|new
name|KerberosAuthentication
argument_list|(
name|configuration
argument_list|,
name|username
argument_list|,
name|keyTabFileLocation
argument_list|)
expr_stmt|;
comment|// when
name|underTest
operator|.
name|loginWithKeytab
argument_list|()
expr_stmt|;
comment|// then
comment|/* exception was thrown */
block|}
block|}
end_class

end_unit

