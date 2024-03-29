begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.service.lra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
package|;
end_package

begin_class
DECL|class|LRAConstants
specifier|public
specifier|final
class|class
name|LRAConstants
block|{
DECL|field|DEFAULT_COORDINATOR_CONTEXT_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_COORDINATOR_CONTEXT_PATH
init|=
literal|"/lra-coordinator"
decl_stmt|;
DECL|field|DEFAULT_LOCAL_PARTICIPANT_CONTEXT_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LOCAL_PARTICIPANT_CONTEXT_PATH
init|=
literal|"/lra-participant"
decl_stmt|;
DECL|field|COORDINATOR_PATH_START
specifier|static
specifier|final
name|String
name|COORDINATOR_PATH_START
init|=
literal|"/start"
decl_stmt|;
DECL|field|COORDINATOR_PATH_CLOSE
specifier|static
specifier|final
name|String
name|COORDINATOR_PATH_CLOSE
init|=
literal|"/close"
decl_stmt|;
DECL|field|COORDINATOR_PATH_CANCEL
specifier|static
specifier|final
name|String
name|COORDINATOR_PATH_CANCEL
init|=
literal|"/cancel"
decl_stmt|;
DECL|field|PARTICIPANT_PATH_COMPENSATE
specifier|static
specifier|final
name|String
name|PARTICIPANT_PATH_COMPENSATE
init|=
literal|"/compensate"
decl_stmt|;
DECL|field|PARTICIPANT_PATH_COMPLETE
specifier|static
specifier|final
name|String
name|PARTICIPANT_PATH_COMPLETE
init|=
literal|"/complete"
decl_stmt|;
DECL|field|HEADER_LINK
specifier|static
specifier|final
name|String
name|HEADER_LINK
init|=
literal|"Link"
decl_stmt|;
DECL|field|HEADER_TIME_LIMIT
specifier|static
specifier|final
name|String
name|HEADER_TIME_LIMIT
init|=
literal|"TimeLimit"
decl_stmt|;
DECL|field|URL_COMPENSATION_KEY
specifier|static
specifier|final
name|String
name|URL_COMPENSATION_KEY
init|=
literal|"Camel-Saga-Compensate"
decl_stmt|;
DECL|field|URL_COMPLETION_KEY
specifier|static
specifier|final
name|String
name|URL_COMPLETION_KEY
init|=
literal|"Camel-Saga-Complete"
decl_stmt|;
DECL|method|LRAConstants ()
specifier|private
name|LRAConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

