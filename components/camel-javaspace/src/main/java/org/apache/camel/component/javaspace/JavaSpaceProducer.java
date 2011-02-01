begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|UID
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|entry
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|lease
operator|.
name|Lease
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|transaction
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|space
operator|.
name|JavaSpace
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
name|camel
operator|.
name|Producer
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
name|component
operator|.
name|bean
operator|.
name|BeanInvocation
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} implementation for JavaSpaces  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JavaSpaceProducer
specifier|public
class|class
name|JavaSpaceProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JavaSpaceProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|transactional
specifier|private
specifier|final
name|boolean
name|transactional
decl_stmt|;
DECL|field|transactionTimeout
specifier|private
specifier|final
name|long
name|transactionTimeout
decl_stmt|;
DECL|field|javaSpace
specifier|private
name|JavaSpace
name|javaSpace
decl_stmt|;
DECL|field|transactionHelper
specifier|private
name|TransactionHelper
name|transactionHelper
decl_stmt|;
DECL|method|JavaSpaceProducer (JavaSpaceEndpoint endpoint)
specifier|public
name|JavaSpaceProducer
parameter_list|(
name|JavaSpaceEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|transactional
operator|=
name|endpoint
operator|.
name|isTransactional
argument_list|()
expr_stmt|;
name|this
operator|.
name|transactionTimeout
operator|=
name|endpoint
operator|.
name|getTransactionTimeout
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
name|Entry
name|entry
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|body
operator|instanceof
name|Entry
operator|)
condition|)
block|{
name|entry
operator|=
operator|new
name|InEntry
argument_list|()
expr_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|BeanInvocation
condition|)
block|{
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|correlationId
operator|=
operator|(
operator|new
name|UID
argument_list|()
operator|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|byte
index|[]
condition|)
block|{
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|binary
operator|=
literal|true
expr_stmt|;
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|buffer
operator|=
operator|(
name|byte
index|[]
operator|)
name|body
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|binary
operator|=
literal|false
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|body
argument_list|)
expr_stmt|;
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|buffer
operator|=
name|bos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|entry
operator|=
operator|(
name|Entry
operator|)
name|body
expr_stmt|;
block|}
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No payload for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Transaction
name|tnx
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|transactionHelper
operator|!=
literal|null
condition|)
block|{
name|tnx
operator|=
name|transactionHelper
operator|.
name|getJiniTransaction
argument_list|(
name|transactionTimeout
argument_list|)
operator|.
name|transaction
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing body : "
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
name|javaSpace
operator|.
name|write
argument_list|(
name|entry
argument_list|,
name|tnx
argument_list|,
name|Lease
operator|.
name|FOREVER
argument_list|)
expr_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|OutEntry
name|tmpl
init|=
operator|new
name|OutEntry
argument_list|()
decl_stmt|;
name|tmpl
operator|.
name|correlationId
operator|=
operator|(
operator|(
name|InEntry
operator|)
name|entry
operator|)
operator|.
name|correlationId
expr_stmt|;
name|OutEntry
name|replyCamelEntry
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|replyCamelEntry
operator|==
literal|null
condition|)
block|{
name|replyCamelEntry
operator|=
operator|(
name|OutEntry
operator|)
name|javaSpace
operator|.
name|take
argument_list|(
name|tmpl
argument_list|,
name|tnx
argument_list|,
literal|100
argument_list|)
expr_stmt|;
block|}
name|Object
name|obj
decl_stmt|;
if|if
condition|(
name|replyCamelEntry
operator|.
name|binary
condition|)
block|{
name|obj
operator|=
name|replyCamelEntry
operator|.
name|buffer
expr_stmt|;
block|}
else|else
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|replyCamelEntry
operator|.
name|buffer
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|ois
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|obj
operator|=
name|ois
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tnx
operator|!=
literal|null
condition|)
block|{
name|tnx
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: There should be a switch to enable/disable using this security hack
name|Utility
operator|.
name|setSecurityPolicy
argument_list|(
literal|"policy.all"
argument_list|,
literal|"policy_producer.all"
argument_list|)
expr_stmt|;
name|javaSpace
operator|=
name|JiniSpaceAccessor
operator|.
name|findSpace
argument_list|(
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getRemaining
argument_list|()
argument_list|,
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getSpaceName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|transactional
condition|)
block|{
name|transactionHelper
operator|=
name|TransactionHelper
operator|.
name|getInstance
argument_list|(
operator|(
operator|(
name|JavaSpaceEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getRemaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|new
name|File
argument_list|(
literal|"policy_producer.all"
argument_list|)
operator|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

