begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|mail
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
name|gae
operator|.
name|mail
operator|.
name|GMailTestUtils
operator|.
name|createEndpoint
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|GMailEndpointTest
specifier|public
class|class
name|GMailEndpointTest
block|{
DECL|field|AMP
specifier|private
specifier|static
specifier|final
name|String
name|AMP
init|=
literal|"&"
decl_stmt|;
annotation|@
name|Test
DECL|method|testPropertiesCustom ()
specifier|public
name|void
name|testPropertiesCustom
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"gmail:user1@gmail.com"
argument_list|)
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
operator|.
name|append
argument_list|(
literal|"subject=test"
argument_list|)
operator|.
name|append
argument_list|(
name|AMP
argument_list|)
operator|.
name|append
argument_list|(
literal|"to=user2@gmail.com"
argument_list|)
operator|.
name|append
argument_list|(
name|AMP
argument_list|)
operator|.
name|append
argument_list|(
literal|"mailServiceRef=#mockMailService"
argument_list|)
operator|.
name|append
argument_list|(
name|AMP
argument_list|)
operator|.
name|append
argument_list|(
literal|"outboundBindingRef=#customBinding"
argument_list|)
decl_stmt|;
name|GMailEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|endpoint
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user1@gmail.com"
argument_list|,
name|endpoint
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user2@gmail.com"
argument_list|,
name|endpoint
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|getOutboundBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GMailBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getOutboundBinding
argument_list|()
operator|instanceof
name|GMailBinding
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getMailService
argument_list|()
operator|instanceof
name|MockMailService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

