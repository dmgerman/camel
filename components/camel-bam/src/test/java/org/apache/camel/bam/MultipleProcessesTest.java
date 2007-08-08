begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
operator|.
name|JuelExpression
operator|.
name|el
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MultipleProcessesTest
specifier|public
class|class
name|MultipleProcessesTest
extends|extends
name|BamRouteTest
block|{
annotation|@
name|Override
DECL|method|testBam ()
specifier|public
name|void
name|testBam
parameter_list|()
throws|throws
name|Exception
block|{
name|overdueEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|overdueEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|predicate
argument_list|(
name|el
argument_list|(
literal|"${in.body.correlationKey == '124'}"
argument_list|)
argument_list|)
expr_stmt|;
name|sendAMessages
argument_list|()
expr_stmt|;
name|sendBMessages
argument_list|()
expr_stmt|;
name|overdueEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendAMessages ()
specifier|protected
name|void
name|sendAMessages
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello id='123'>A</hello>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello id='124'>B</hello>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello id='125'>C</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBMessages ()
specifier|protected
name|void
name|sendBMessages
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"<hello id='123'>A</hello>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"<hello id='125'>C</hello>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

