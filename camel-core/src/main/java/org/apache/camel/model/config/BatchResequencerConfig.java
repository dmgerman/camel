begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|Resequencer
import|;
end_import

begin_comment
comment|/**  * Defines the configuration parameters for the batch-processing  * {@link Resequencer}. Usage example:  *   *<pre>  * from(&quot;direct:start&quot;).resequencer(body()).batch(  *         BatchResequencerConfig.getDefault()).to(&quot;mock:result&quot;)  *</pre>  * is equivalent to  *   *<pre>  * from(&quot;direct:start&quot;).resequencer(body()).batch().to(&quot;mock:result&quot;)  *</pre>  *   * or  *   *<pre>  * from(&quot;direct:start&quot;).resequencer(body()).to(&quot;mock:result&quot;)  *</pre>  *   * Custom values for<code>batchSize</code> and<code>batchTimeout</code>  * can be set like in this example:  *   *<pre>  * from(&quot;direct:start&quot;).resequencer(body()).batch(  *         new BatchResequencerConfig(300, 400L)).to(&quot;mock:result&quot;)  *</pre>  *   * @author Martin Krasser  *   * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BatchResequencerConfig
specifier|public
class|class
name|BatchResequencerConfig
block|{
annotation|@
name|XmlAttribute
DECL|field|batchSize
specifier|private
name|Integer
name|batchSize
decl_stmt|;
comment|// optional XML attribute requires wrapper object
annotation|@
name|XmlAttribute
DECL|field|batchTimeout
specifier|private
name|Long
name|batchTimeout
decl_stmt|;
comment|// optional XML attribute requires wrapper object
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
comment|/**      * Creates a new {@link BatchResequencerConfig} instance using the given      * values for<code>batchSize</code> and<code>batchTimeout</code>.      *       * @param batchSize      *            size of the batch to be re-ordered.      * @param batchTimeout      *            timeout for collecting elements to be re-ordered.      */
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
name|batchSize
expr_stmt|;
name|this
operator|.
name|batchTimeout
operator|=
name|batchTimeout
expr_stmt|;
block|}
comment|/**      * Returns a new {@link BatchResequencerConfig} instance using default      * values for<code>batchSize</code> (100) and<code>batchTimeout</code>      * (1000L).      *       * @return a default {@link BatchResequencerConfig}.      */
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
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
DECL|method|setBatchSize (int batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
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
name|long
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
DECL|method|setBatchTimeout (long batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|long
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
block|}
end_class

end_unit

