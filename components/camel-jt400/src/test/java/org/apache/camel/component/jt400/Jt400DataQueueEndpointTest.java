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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
operator|.
name|Jt400DataQueueEndpoint
operator|.
name|Format
import|;
end_import

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

begin_comment
comment|/**  * Test case for {@link Jt400DataQueueEndpoint}  */
end_comment

begin_class
DECL|class|Jt400DataQueueEndpointTest
specifier|public
class|class
name|Jt400DataQueueEndpointTest
extends|extends
name|Jt400TestSupport
block|{
DECL|field|endpoint
specifier|private
name|Jt400DataQueueEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Override
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
name|endpoint
operator|=
operator|(
name|Jt400DataQueueEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"jt400://user:password@host/qsys.lib/library.lib/queue.dtaq?ccsid=500&format=binary&guiAvailable=true&connectionPool=#mockPool"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check that the AS/400 connection is correctly configured for the URL      */
annotation|@
name|Test
DECL|method|testSystemConfiguration ()
specifier|public
name|void
name|testSystemConfiguration
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"USER"
argument_list|,
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"host"
argument_list|,
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|getSystemName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|getCcsid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Format
operator|.
name|binary
argument_list|,
name|endpoint
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|isGuiAvailable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Endpoint[jt400://user:******@host/qsys.lib/library.lib/queue.dtaq?ccsid=500&connectionPool=%23mockPool&format=binary&guiAvailable=true]"
argument_list|,
name|endpoint
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

