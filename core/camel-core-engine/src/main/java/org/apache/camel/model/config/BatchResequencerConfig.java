begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|config
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchemaType
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Configures batch-processing resequence eip.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,resequence"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"batch-config"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
class|class
DECL|class|BatchResequencerConfig
name|BatchResequencerConfig
extends|extends
name|ResequencerConfig
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"100"
argument_list|,
name|javaType
operator|=
literal|"java.lang.Integer"
argument_list|)
DECL|field|batchSize
specifier|private
name|String
name|batchSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|,
name|javaType
operator|=
literal|"java.lang.Long"
argument_list|)
DECL|field|batchTimeout
specifier|private
name|String
name|batchTimeout
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|allowDuplicates
specifier|private
name|String
name|allowDuplicates
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|reverse
specifier|private
name|String
name|reverse
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|ignoreInvalidExchanges
specifier|private
name|String
name|ignoreInvalidExchanges
decl_stmt|;
comment|/**      * Creates a new {@link BatchResequencerConfig} instance using default      * values for<code>batchSize</code> (100) and<code>batchTimeout</code>      * (1000L).      */
DECL|method|BatchResequencerConfig ()
specifier|public
name|BatchResequencerConfig
parameter_list|()
block|{
name|this
argument_list|(
literal|100
argument_list|,
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link BatchResequencerConfig} instance using the given      * values for<code>batchSize</code> and<code>batchTimeout</code>.      *      * @param batchSize size of the batch to be re-ordered.      * @param batchTimeout timeout for collecting elements to be re-ordered.      */
DECL|method|BatchResequencerConfig (int batchSize, long batchTimeout)
specifier|public
name|BatchResequencerConfig
parameter_list|(
name|int
name|batchSize
parameter_list|,
name|long
name|batchTimeout
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|batchTimeout
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|batchTimeout
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a new {@link BatchResequencerConfig} instance using default      * values for<code>batchSize</code> (100) and<code>batchTimeout</code>      * (1000L).      *      * @return a default {@link BatchResequencerConfig}.      */
DECL|method|getDefault ()
specifier|public
specifier|static
name|BatchResequencerConfig
name|getDefault
parameter_list|()
block|{
return|return
operator|new
name|BatchResequencerConfig
argument_list|()
return|;
block|}
DECL|method|getBatchSize ()
specifier|public
name|String
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
comment|/**      * Sets the size of the batch to be re-ordered. The default size is 100.      */
DECL|method|setBatchSize (String batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|String
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
DECL|method|getBatchTimeout ()
specifier|public
name|String
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
comment|/**      * Sets the timeout for collecting elements to be re-ordered. The default      * timeout is 1000 msec.      */
DECL|method|setBatchTimeout (String batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|String
name|batchTimeout
parameter_list|)
block|{
name|this
operator|.
name|batchTimeout
operator|=
name|batchTimeout
expr_stmt|;
block|}
DECL|method|getAllowDuplicates ()
specifier|public
name|String
name|getAllowDuplicates
parameter_list|()
block|{
return|return
name|allowDuplicates
return|;
block|}
comment|/**      * Whether to allow duplicates.      */
DECL|method|setAllowDuplicates (String allowDuplicates)
specifier|public
name|void
name|setAllowDuplicates
parameter_list|(
name|String
name|allowDuplicates
parameter_list|)
block|{
name|this
operator|.
name|allowDuplicates
operator|=
name|allowDuplicates
expr_stmt|;
block|}
DECL|method|getReverse ()
specifier|public
name|String
name|getReverse
parameter_list|()
block|{
return|return
name|reverse
return|;
block|}
comment|/**      * Whether to reverse the ordering.      */
DECL|method|setReverse (String reverse)
specifier|public
name|void
name|setReverse
parameter_list|(
name|String
name|reverse
parameter_list|)
block|{
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidExchanges ()
specifier|public
name|String
name|getIgnoreInvalidExchanges
parameter_list|()
block|{
return|return
name|ignoreInvalidExchanges
return|;
block|}
comment|/**      * Whether to ignore invalid exchanges      */
DECL|method|setIgnoreInvalidExchanges (String ignoreInvalidExchanges)
specifier|public
name|void
name|setIgnoreInvalidExchanges
parameter_list|(
name|String
name|ignoreInvalidExchanges
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidExchanges
operator|=
name|ignoreInvalidExchanges
expr_stmt|;
block|}
block|}
end_class

end_unit

