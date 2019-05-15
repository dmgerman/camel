begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
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
name|component
operator|.
name|docker
operator|.
name|exception
operator|.
name|DockerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
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
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Validates the {@link DockerClientProfile}  */
end_comment

begin_class
DECL|class|DockerClientProfileTest
specifier|public
class|class
name|DockerClientProfileTest
block|{
annotation|@
name|Rule
DECL|field|expectedException
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|clientProfileTest ()
specifier|public
name|void
name|clientProfileTest
parameter_list|()
block|{
name|String
name|host
init|=
literal|"host"
decl_stmt|;
name|String
name|email
init|=
literal|"docker@camel.apache.org"
decl_stmt|;
name|String
name|username
init|=
literal|"user"
decl_stmt|;
name|String
name|password
init|=
literal|"password"
decl_stmt|;
name|Integer
name|port
init|=
literal|2241
decl_stmt|;
name|Integer
name|requestTimeout
init|=
literal|40
decl_stmt|;
name|boolean
name|secure
init|=
literal|true
decl_stmt|;
name|String
name|certPath
init|=
literal|"/docker/cert/path"
decl_stmt|;
name|String
name|cmdExecFactory
init|=
name|DockerConstants
operator|.
name|DEFAULT_CMD_EXEC_FACTORY
decl_stmt|;
name|DockerClientProfile
name|clientProfile1
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|clientProfile1
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setEmail
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setRequestTimeout
argument_list|(
name|requestTimeout
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setSecure
argument_list|(
name|secure
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setCertPath
argument_list|(
name|certPath
argument_list|)
expr_stmt|;
name|clientProfile1
operator|.
name|setCmdExecFactory
argument_list|(
name|cmdExecFactory
argument_list|)
expr_stmt|;
name|DockerClientProfile
name|clientProfile2
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|clientProfile2
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setEmail
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setRequestTimeout
argument_list|(
name|requestTimeout
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setSecure
argument_list|(
name|secure
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setCertPath
argument_list|(
name|certPath
argument_list|)
expr_stmt|;
name|clientProfile2
operator|.
name|setCmdExecFactory
argument_list|(
name|cmdExecFactory
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|clientProfile1
argument_list|,
name|clientProfile2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clientProfileUrlTest ()
specifier|public
name|void
name|clientProfileUrlTest
parameter_list|()
throws|throws
name|DockerException
block|{
name|DockerClientProfile
name|profile
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|profile
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setPort
argument_list|(
literal|2375
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tcp://localhost:2375"
argument_list|,
name|profile
operator|.
name|toUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clientProfileNoPortSpecifiedUrlTest ()
specifier|public
name|void
name|clientProfileNoPortSpecifiedUrlTest
parameter_list|()
throws|throws
name|DockerException
block|{
name|DockerClientProfile
name|profile
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|profile
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expectMessage
argument_list|(
literal|"port must be specified"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tcp://localhost:2375"
argument_list|,
name|profile
operator|.
name|toUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clientProfileWithSocketUrlTest ()
specifier|public
name|void
name|clientProfileWithSocketUrlTest
parameter_list|()
throws|throws
name|DockerException
block|{
name|DockerClientProfile
name|profile
init|=
operator|new
name|DockerClientProfile
argument_list|()
decl_stmt|;
name|profile
operator|.
name|setHost
argument_list|(
literal|"/var/run/docker.sock"
argument_list|)
expr_stmt|;
comment|// Port should be ignored
name|profile
operator|.
name|setPort
argument_list|(
literal|2375
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setSocket
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unix:///var/run/docker.sock"
argument_list|,
name|profile
operator|.
name|toUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

