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
DECL|class|Jt400DataQueueProducerTest
specifier|public
class|class
name|Jt400DataQueueProducerTest
extends|extends
name|Jt400TestSupport
block|{
DECL|field|PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"p4ssw0rd"
decl_stmt|;
DECL|field|producer
specifier|private
name|Jt400DataQueueProducer
name|producer
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
name|Jt400Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"jt400://user:"
operator|+
name|PASSWORD
operator|+
literal|"@host/qsys.lib/library.lib/queue.dtaq?connectionPool=#mockPool"
argument_list|,
name|Jt400Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|producer
operator|=
operator|new
name|Jt400DataQueueProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToStringHidesPassword ()
specifier|public
name|void
name|testToStringHidesPassword
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|producer
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|PASSWORD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

