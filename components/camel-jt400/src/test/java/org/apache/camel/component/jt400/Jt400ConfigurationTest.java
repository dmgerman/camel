begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
DECL|class|Jt400ConfigurationTest
specifier|public
class|class
name|Jt400ConfigurationTest
extends|extends
name|Jt400TestSupport
block|{
DECL|field|jt400Configuration
specifier|private
name|Jt400Configuration
name|jt400Configuration
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|jt400Configuration
operator|=
operator|new
name|Jt400Configuration
argument_list|(
literal|"jt400://USER:password@host/QSYS.LIB/LIBRARY.LIB/QUEUE.DTAQ"
argument_list|,
name|getConnectionPool
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultSecured ()
specifier|public
name|void
name|testDefaultSecured
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|jt400Configuration
operator|.
name|isSecured
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSystemName ()
specifier|public
name|void
name|testSystemName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"host"
argument_list|,
name|jt400Configuration
operator|.
name|getSystemName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUserID ()
specifier|public
name|void
name|testUserID
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"USER"
argument_list|,
name|jt400Configuration
operator|.
name|getUserID
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPassword ()
specifier|public
name|void
name|testPassword
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"password"
argument_list|,
name|jt400Configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testObjectPath ()
specifier|public
name|void
name|testObjectPath
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/QSYS.LIB/LIBRARY.LIB/QUEUE.DTAQ"
argument_list|,
name|jt400Configuration
operator|.
name|getObjectPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultCcsid ()
specifier|public
name|void
name|testDefaultCcsid
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|jt400Configuration
operator|.
name|getCssid
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultFormat ()
specifier|public
name|void
name|testDefaultFormat
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Jt400Configuration
operator|.
name|Format
operator|.
name|text
argument_list|,
name|jt400Configuration
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultGuiAvailable ()
specifier|public
name|void
name|testDefaultGuiAvailable
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|jt400Configuration
operator|.
name|isGuiAvailable
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

