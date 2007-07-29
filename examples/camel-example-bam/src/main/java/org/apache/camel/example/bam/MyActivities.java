begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|bam
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
name|bam
operator|.
name|ActivityBuilder
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
name|bam
operator|.
name|ProcessBuilder
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
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
name|util
operator|.
name|Time
operator|.
name|seconds
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
name|Processor
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
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
DECL|class|MyActivities
specifier|public
class|class
name|MyActivities
extends|extends
name|ProcessBuilder
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MyActivities
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MyActivities (JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate)
specifier|protected
name|MyActivities
parameter_list|(
name|JpaTemplate
name|jpaTemplate
parameter_list|,
name|TransactionTemplate
name|transactionTemplate
parameter_list|)
block|{
name|super
argument_list|(
name|jpaTemplate
argument_list|,
name|transactionTemplate
argument_list|)
expr_stmt|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets define some activities, correlating on an XPath on the message bodies
name|ActivityBuilder
name|purchaseOrder
init|=
name|activity
argument_list|(
literal|"file:src/data/purchaseOrders?noop=true"
argument_list|)
operator|.
name|correlate
argument_list|(
name|xpath
argument_list|(
literal|"/purchaseOrder/@id"
argument_list|)
argument_list|)
decl_stmt|;
name|ActivityBuilder
name|invoice
init|=
name|activity
argument_list|(
literal|"file:src/data/invoices?noop=true"
argument_list|)
operator|.
name|correlate
argument_list|(
name|xpath
argument_list|(
literal|"/invoice/@purchaseOrderId"
argument_list|)
argument_list|)
decl_stmt|;
comment|// now lets add some rules
name|invoice
operator|.
name|starts
argument_list|()
operator|.
name|after
argument_list|(
name|purchaseOrder
operator|.
name|completes
argument_list|()
argument_list|)
operator|.
name|expectWithin
argument_list|(
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|errorIfOver
argument_list|(
name|seconds
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:failures"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:failures"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Failed process!: "
operator|+
name|exchange
operator|+
literal|" with body: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

