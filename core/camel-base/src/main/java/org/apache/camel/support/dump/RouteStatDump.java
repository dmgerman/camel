begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.dump
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|dump
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|XmlElement
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
name|XmlElementWrapper
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
name|XmlElements
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

begin_comment
comment|/**  * A model of a route stat dump from {@link org.apache.camel.api.management.mbean.ManagedRouteMBean#dumpRouteStatsAsXml(boolean, boolean)}.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"routeStat"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RouteStatDump
specifier|public
specifier|final
class|class
name|RouteStatDump
block|{
annotation|@
name|XmlAttribute
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|exchangesCompleted
specifier|private
name|Long
name|exchangesCompleted
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|exchangesFailed
specifier|private
name|Long
name|exchangesFailed
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|failuresHandled
specifier|private
name|Long
name|failuresHandled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|redeliveries
specifier|private
name|Long
name|redeliveries
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|minProcessingTime
specifier|private
name|Long
name|minProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxProcessingTime
specifier|private
name|Long
name|maxProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|totalProcessingTime
specifier|private
name|Long
name|totalProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lastProcessingTime
specifier|private
name|Long
name|lastProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|deltaProcessingTime
specifier|private
name|Long
name|deltaProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|meanProcessingTime
specifier|private
name|Long
name|meanProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|exchangesInflight
specifier|private
name|Long
name|exchangesInflight
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|selfProcessingTime
specifier|private
name|Long
name|selfProcessingTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|startTimestamp
specifier|private
name|String
name|startTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|resetTimestamp
specifier|private
name|String
name|resetTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|firstExchangeCompletedTimestamp
specifier|private
name|String
name|firstExchangeCompletedTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|firstExchangeCompletedExchangeId
specifier|private
name|String
name|firstExchangeCompletedExchangeId
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|firstExchangeFailureTimestamp
specifier|private
name|String
name|firstExchangeFailureTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|firstExchangeFailureExchangeId
specifier|private
name|String
name|firstExchangeFailureExchangeId
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lastExchangeCompletedTimestamp
specifier|private
name|String
name|lastExchangeCompletedTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lastExchangeCompletedExchangeId
specifier|private
name|String
name|lastExchangeCompletedExchangeId
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lastExchangeFailureTimestamp
specifier|private
name|String
name|lastExchangeFailureTimestamp
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lastExchangeFailureExchangeId
specifier|private
name|String
name|lastExchangeFailureExchangeId
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"processorStats"
argument_list|)
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|type
operator|=
name|ProcessorStatDump
operator|.
name|class
argument_list|,
name|name
operator|=
literal|"processorStat"
argument_list|)
block|}
argument_list|)
DECL|field|processorStats
specifier|private
name|List
argument_list|<
name|ProcessorStatDump
argument_list|>
name|processorStats
decl_stmt|;
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|setState (String state)
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getExchangesCompleted ()
specifier|public
name|Long
name|getExchangesCompleted
parameter_list|()
block|{
return|return
name|exchangesCompleted
return|;
block|}
DECL|method|setExchangesCompleted (Long exchangesCompleted)
specifier|public
name|void
name|setExchangesCompleted
parameter_list|(
name|Long
name|exchangesCompleted
parameter_list|)
block|{
name|this
operator|.
name|exchangesCompleted
operator|=
name|exchangesCompleted
expr_stmt|;
block|}
DECL|method|getExchangesFailed ()
specifier|public
name|Long
name|getExchangesFailed
parameter_list|()
block|{
return|return
name|exchangesFailed
return|;
block|}
DECL|method|setExchangesFailed (Long exchangesFailed)
specifier|public
name|void
name|setExchangesFailed
parameter_list|(
name|Long
name|exchangesFailed
parameter_list|)
block|{
name|this
operator|.
name|exchangesFailed
operator|=
name|exchangesFailed
expr_stmt|;
block|}
DECL|method|getFailuresHandled ()
specifier|public
name|Long
name|getFailuresHandled
parameter_list|()
block|{
return|return
name|failuresHandled
return|;
block|}
DECL|method|setFailuresHandled (Long failuresHandled)
specifier|public
name|void
name|setFailuresHandled
parameter_list|(
name|Long
name|failuresHandled
parameter_list|)
block|{
name|this
operator|.
name|failuresHandled
operator|=
name|failuresHandled
expr_stmt|;
block|}
DECL|method|getRedeliveries ()
specifier|public
name|Long
name|getRedeliveries
parameter_list|()
block|{
return|return
name|redeliveries
return|;
block|}
DECL|method|setRedeliveries (Long redeliveries)
specifier|public
name|void
name|setRedeliveries
parameter_list|(
name|Long
name|redeliveries
parameter_list|)
block|{
name|this
operator|.
name|redeliveries
operator|=
name|redeliveries
expr_stmt|;
block|}
DECL|method|getMinProcessingTime ()
specifier|public
name|Long
name|getMinProcessingTime
parameter_list|()
block|{
return|return
name|minProcessingTime
return|;
block|}
DECL|method|setMinProcessingTime (Long minProcessingTime)
specifier|public
name|void
name|setMinProcessingTime
parameter_list|(
name|Long
name|minProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|minProcessingTime
operator|=
name|minProcessingTime
expr_stmt|;
block|}
DECL|method|getMaxProcessingTime ()
specifier|public
name|Long
name|getMaxProcessingTime
parameter_list|()
block|{
return|return
name|maxProcessingTime
return|;
block|}
DECL|method|setMaxProcessingTime (Long maxProcessingTime)
specifier|public
name|void
name|setMaxProcessingTime
parameter_list|(
name|Long
name|maxProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|maxProcessingTime
operator|=
name|maxProcessingTime
expr_stmt|;
block|}
DECL|method|getTotalProcessingTime ()
specifier|public
name|Long
name|getTotalProcessingTime
parameter_list|()
block|{
return|return
name|totalProcessingTime
return|;
block|}
DECL|method|setTotalProcessingTime (Long totalProcessingTime)
specifier|public
name|void
name|setTotalProcessingTime
parameter_list|(
name|Long
name|totalProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|totalProcessingTime
operator|=
name|totalProcessingTime
expr_stmt|;
block|}
DECL|method|getLastProcessingTime ()
specifier|public
name|Long
name|getLastProcessingTime
parameter_list|()
block|{
return|return
name|lastProcessingTime
return|;
block|}
DECL|method|setLastProcessingTime (Long lastProcessingTime)
specifier|public
name|void
name|setLastProcessingTime
parameter_list|(
name|Long
name|lastProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|lastProcessingTime
operator|=
name|lastProcessingTime
expr_stmt|;
block|}
DECL|method|getDeltaProcessingTime ()
specifier|public
name|Long
name|getDeltaProcessingTime
parameter_list|()
block|{
return|return
name|deltaProcessingTime
return|;
block|}
DECL|method|setDeltaProcessingTime (Long deltaProcessingTime)
specifier|public
name|void
name|setDeltaProcessingTime
parameter_list|(
name|Long
name|deltaProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|deltaProcessingTime
operator|=
name|deltaProcessingTime
expr_stmt|;
block|}
DECL|method|getMeanProcessingTime ()
specifier|public
name|Long
name|getMeanProcessingTime
parameter_list|()
block|{
return|return
name|meanProcessingTime
return|;
block|}
DECL|method|setMeanProcessingTime (Long meanProcessingTime)
specifier|public
name|void
name|setMeanProcessingTime
parameter_list|(
name|Long
name|meanProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|meanProcessingTime
operator|=
name|meanProcessingTime
expr_stmt|;
block|}
DECL|method|getSelfProcessingTime ()
specifier|public
name|Long
name|getSelfProcessingTime
parameter_list|()
block|{
return|return
name|selfProcessingTime
return|;
block|}
DECL|method|setSelfProcessingTime (Long selfProcessingTime)
specifier|public
name|void
name|setSelfProcessingTime
parameter_list|(
name|Long
name|selfProcessingTime
parameter_list|)
block|{
name|this
operator|.
name|selfProcessingTime
operator|=
name|selfProcessingTime
expr_stmt|;
block|}
DECL|method|getExchangesInflight ()
specifier|public
name|Long
name|getExchangesInflight
parameter_list|()
block|{
return|return
name|exchangesInflight
return|;
block|}
DECL|method|setExchangesInflight (Long exchangesInflight)
specifier|public
name|void
name|setExchangesInflight
parameter_list|(
name|Long
name|exchangesInflight
parameter_list|)
block|{
name|this
operator|.
name|exchangesInflight
operator|=
name|exchangesInflight
expr_stmt|;
block|}
DECL|method|getStartTimestamp ()
specifier|public
name|String
name|getStartTimestamp
parameter_list|()
block|{
return|return
name|startTimestamp
return|;
block|}
DECL|method|setStartTimestamp (String startTimestamp)
specifier|public
name|void
name|setStartTimestamp
parameter_list|(
name|String
name|startTimestamp
parameter_list|)
block|{
name|this
operator|.
name|startTimestamp
operator|=
name|startTimestamp
expr_stmt|;
block|}
DECL|method|getResetTimestamp ()
specifier|public
name|String
name|getResetTimestamp
parameter_list|()
block|{
return|return
name|resetTimestamp
return|;
block|}
DECL|method|setResetTimestamp (String resetTimestamp)
specifier|public
name|void
name|setResetTimestamp
parameter_list|(
name|String
name|resetTimestamp
parameter_list|)
block|{
name|this
operator|.
name|resetTimestamp
operator|=
name|resetTimestamp
expr_stmt|;
block|}
DECL|method|getFirstExchangeCompletedTimestamp ()
specifier|public
name|String
name|getFirstExchangeCompletedTimestamp
parameter_list|()
block|{
return|return
name|firstExchangeCompletedTimestamp
return|;
block|}
DECL|method|setFirstExchangeCompletedTimestamp (String firstExchangeCompletedTimestamp)
specifier|public
name|void
name|setFirstExchangeCompletedTimestamp
parameter_list|(
name|String
name|firstExchangeCompletedTimestamp
parameter_list|)
block|{
name|this
operator|.
name|firstExchangeCompletedTimestamp
operator|=
name|firstExchangeCompletedTimestamp
expr_stmt|;
block|}
DECL|method|getFirstExchangeCompletedExchangeId ()
specifier|public
name|String
name|getFirstExchangeCompletedExchangeId
parameter_list|()
block|{
return|return
name|firstExchangeCompletedExchangeId
return|;
block|}
DECL|method|setFirstExchangeCompletedExchangeId (String firstExchangeCompletedExchangeId)
specifier|public
name|void
name|setFirstExchangeCompletedExchangeId
parameter_list|(
name|String
name|firstExchangeCompletedExchangeId
parameter_list|)
block|{
name|this
operator|.
name|firstExchangeCompletedExchangeId
operator|=
name|firstExchangeCompletedExchangeId
expr_stmt|;
block|}
DECL|method|getFirstExchangeFailureTimestamp ()
specifier|public
name|String
name|getFirstExchangeFailureTimestamp
parameter_list|()
block|{
return|return
name|firstExchangeFailureTimestamp
return|;
block|}
DECL|method|setFirstExchangeFailureTimestamp (String firstExchangeFailureTimestamp)
specifier|public
name|void
name|setFirstExchangeFailureTimestamp
parameter_list|(
name|String
name|firstExchangeFailureTimestamp
parameter_list|)
block|{
name|this
operator|.
name|firstExchangeFailureTimestamp
operator|=
name|firstExchangeFailureTimestamp
expr_stmt|;
block|}
DECL|method|getFirstExchangeFailureExchangeId ()
specifier|public
name|String
name|getFirstExchangeFailureExchangeId
parameter_list|()
block|{
return|return
name|firstExchangeFailureExchangeId
return|;
block|}
DECL|method|setFirstExchangeFailureExchangeId (String firstExchangeFailureExchangeId)
specifier|public
name|void
name|setFirstExchangeFailureExchangeId
parameter_list|(
name|String
name|firstExchangeFailureExchangeId
parameter_list|)
block|{
name|this
operator|.
name|firstExchangeFailureExchangeId
operator|=
name|firstExchangeFailureExchangeId
expr_stmt|;
block|}
DECL|method|getLastExchangeCompletedTimestamp ()
specifier|public
name|String
name|getLastExchangeCompletedTimestamp
parameter_list|()
block|{
return|return
name|lastExchangeCompletedTimestamp
return|;
block|}
DECL|method|setLastExchangeCompletedTimestamp (String lastExchangeCompletedTimestamp)
specifier|public
name|void
name|setLastExchangeCompletedTimestamp
parameter_list|(
name|String
name|lastExchangeCompletedTimestamp
parameter_list|)
block|{
name|this
operator|.
name|lastExchangeCompletedTimestamp
operator|=
name|lastExchangeCompletedTimestamp
expr_stmt|;
block|}
DECL|method|getLastExchangeCompletedExchangeId ()
specifier|public
name|String
name|getLastExchangeCompletedExchangeId
parameter_list|()
block|{
return|return
name|lastExchangeCompletedExchangeId
return|;
block|}
DECL|method|setLastExchangeCompletedExchangeId (String lastExchangeCompletedExchangeId)
specifier|public
name|void
name|setLastExchangeCompletedExchangeId
parameter_list|(
name|String
name|lastExchangeCompletedExchangeId
parameter_list|)
block|{
name|this
operator|.
name|lastExchangeCompletedExchangeId
operator|=
name|lastExchangeCompletedExchangeId
expr_stmt|;
block|}
DECL|method|getLastExchangeFailureTimestamp ()
specifier|public
name|String
name|getLastExchangeFailureTimestamp
parameter_list|()
block|{
return|return
name|lastExchangeFailureTimestamp
return|;
block|}
DECL|method|setLastExchangeFailureTimestamp (String lastExchangeFailureTimestamp)
specifier|public
name|void
name|setLastExchangeFailureTimestamp
parameter_list|(
name|String
name|lastExchangeFailureTimestamp
parameter_list|)
block|{
name|this
operator|.
name|lastExchangeFailureTimestamp
operator|=
name|lastExchangeFailureTimestamp
expr_stmt|;
block|}
DECL|method|getLastExchangeFailureExchangeId ()
specifier|public
name|String
name|getLastExchangeFailureExchangeId
parameter_list|()
block|{
return|return
name|lastExchangeFailureExchangeId
return|;
block|}
DECL|method|setLastExchangeFailureExchangeId (String lastExchangeFailureExchangeId)
specifier|public
name|void
name|setLastExchangeFailureExchangeId
parameter_list|(
name|String
name|lastExchangeFailureExchangeId
parameter_list|)
block|{
name|this
operator|.
name|lastExchangeFailureExchangeId
operator|=
name|lastExchangeFailureExchangeId
expr_stmt|;
block|}
DECL|method|getProcessorStats ()
specifier|public
name|List
argument_list|<
name|ProcessorStatDump
argument_list|>
name|getProcessorStats
parameter_list|()
block|{
return|return
name|processorStats
return|;
block|}
DECL|method|setProcessorStats (List<ProcessorStatDump> processorStats)
specifier|public
name|void
name|setProcessorStats
parameter_list|(
name|List
argument_list|<
name|ProcessorStatDump
argument_list|>
name|processorStats
parameter_list|)
block|{
name|this
operator|.
name|processorStats
operator|=
name|processorStats
expr_stmt|;
block|}
block|}
end_class

end_unit
