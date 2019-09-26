begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
package|;
end_package

begin_comment
comment|// CHECKSTYLE:OFF
end_comment

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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|Expression
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|dsl
operator|.
name|*
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
name|support
operator|.
name|ExpressionAdapter
import|;
end_import

begin_interface
DECL|interface|EndpointBuilderFactory
specifier|public
interface|interface
name|EndpointBuilderFactory
extends|extends
name|AMQPEndpointBuilderFactory
extends|,
name|AS2EndpointBuilderFactory
extends|,
name|ActiveMQEndpointBuilderFactory
extends|,
name|AhcEndpointBuilderFactory
extends|,
name|ApnsEndpointBuilderFactory
extends|,
name|AsteriskEndpointBuilderFactory
extends|,
name|AtmosEndpointBuilderFactory
extends|,
name|AtmosphereWebsocketEndpointBuilderFactory
extends|,
name|AtomEndpointBuilderFactory
extends|,
name|AtomixMapEndpointBuilderFactory
extends|,
name|AtomixMessagingEndpointBuilderFactory
extends|,
name|AtomixMultiMapEndpointBuilderFactory
extends|,
name|AtomixQueueEndpointBuilderFactory
extends|,
name|AtomixSetEndpointBuilderFactory
extends|,
name|AtomixValueEndpointBuilderFactory
extends|,
name|AvroEndpointBuilderFactory
extends|,
name|BeanEndpointBuilderFactory
extends|,
name|BeanValidatorEndpointBuilderFactory
extends|,
name|BeanstalkEndpointBuilderFactory
extends|,
name|BlobServiceEndpointBuilderFactory
extends|,
name|BonitaEndpointBuilderFactory
extends|,
name|BoxEndpointBuilderFactory
extends|,
name|BraintreeEndpointBuilderFactory
extends|,
name|BrowseEndpointBuilderFactory
extends|,
name|CMEndpointBuilderFactory
extends|,
name|CMISEndpointBuilderFactory
extends|,
name|CaffeineCacheEndpointBuilderFactory
extends|,
name|CaffeineLoadCacheEndpointBuilderFactory
extends|,
name|CassandraEndpointBuilderFactory
extends|,
name|ChatScriptEndpointBuilderFactory
extends|,
name|ChunkEndpointBuilderFactory
extends|,
name|CinderEndpointBuilderFactory
extends|,
name|ClassEndpointBuilderFactory
extends|,
name|ClientEndpointBuilderFactory
extends|,
name|CoAPEndpointBuilderFactory
extends|,
name|CometdEndpointBuilderFactory
extends|,
name|ConsulEndpointBuilderFactory
extends|,
name|ControlBusEndpointBuilderFactory
extends|,
name|CordaEndpointBuilderFactory
extends|,
name|CouchDbEndpointBuilderFactory
extends|,
name|CouchbaseEndpointBuilderFactory
extends|,
name|CryptoCmsEndpointBuilderFactory
extends|,
name|CwEndpointBuilderFactory
extends|,
name|CxfEndpointBuilderFactory
extends|,
name|CxfRsEndpointBuilderFactory
extends|,
name|DataFormatEndpointBuilderFactory
extends|,
name|DataSetEndpointBuilderFactory
extends|,
name|DataSetTestEndpointBuilderFactory
extends|,
name|DdbEndpointBuilderFactory
extends|,
name|DdbStreamEndpointBuilderFactory
extends|,
name|DigitalOceanEndpointBuilderFactory
extends|,
name|DigitalSignatureEndpointBuilderFactory
extends|,
name|DirectEndpointBuilderFactory
extends|,
name|DirectVmEndpointBuilderFactory
extends|,
name|DisruptorEndpointBuilderFactory
extends|,
name|DisruptorVmEndpointBuilderFactory
extends|,
name|DnsEndpointBuilderFactory
extends|,
name|DockerEndpointBuilderFactory
extends|,
name|DozerEndpointBuilderFactory
extends|,
name|DrillEndpointBuilderFactory
extends|,
name|DropboxEndpointBuilderFactory
extends|,
name|EC2EndpointBuilderFactory
extends|,
name|ECSEndpointBuilderFactory
extends|,
name|EKSEndpointBuilderFactory
extends|,
name|EhcacheEndpointBuilderFactory
extends|,
name|ElasticsearchEndpointBuilderFactory
extends|,
name|ElsqlEndpointBuilderFactory
extends|,
name|EtcdEndpointBuilderFactory
extends|,
name|EventAdminEndpointBuilderFactory
extends|,
name|EventEndpointBuilderFactory
extends|,
name|ExecEndpointBuilderFactory
extends|,
name|FacebookEndpointBuilderFactory
extends|,
name|FhirEndpointBuilderFactory
extends|,
name|FileEndpointBuilderFactory
extends|,
name|FlatpackEndpointBuilderFactory
extends|,
name|FlinkEndpointBuilderFactory
extends|,
name|FopEndpointBuilderFactory
extends|,
name|FreemarkerEndpointBuilderFactory
extends|,
name|FtpEndpointBuilderFactory
extends|,
name|FtpsEndpointBuilderFactory
extends|,
name|GangliaEndpointBuilderFactory
extends|,
name|GeoCoderEndpointBuilderFactory
extends|,
name|GitEndpointBuilderFactory
extends|,
name|GitHubEndpointBuilderFactory
extends|,
name|GlanceEndpointBuilderFactory
extends|,
name|GoogleBigQueryEndpointBuilderFactory
extends|,
name|GoogleBigQuerySQLEndpointBuilderFactory
extends|,
name|GoogleCalendarEndpointBuilderFactory
extends|,
name|GoogleCalendarStreamEndpointBuilderFactory
extends|,
name|GoogleDriveEndpointBuilderFactory
extends|,
name|GoogleMailEndpointBuilderFactory
extends|,
name|GoogleMailStreamEndpointBuilderFactory
extends|,
name|GooglePubsubEndpointBuilderFactory
extends|,
name|GoogleSheetsEndpointBuilderFactory
extends|,
name|GoogleSheetsStreamEndpointBuilderFactory
extends|,
name|GoraEndpointBuilderFactory
extends|,
name|GrapeEndpointBuilderFactory
extends|,
name|GridFsEndpointBuilderFactory
extends|,
name|GrpcEndpointBuilderFactory
extends|,
name|GuavaEventBusEndpointBuilderFactory
extends|,
name|HBaseEndpointBuilderFactory
extends|,
name|HazelcastAtomicnumberEndpointBuilderFactory
extends|,
name|HazelcastInstanceEndpointBuilderFactory
extends|,
name|HazelcastListEndpointBuilderFactory
extends|,
name|HazelcastMapEndpointBuilderFactory
extends|,
name|HazelcastMultimapEndpointBuilderFactory
extends|,
name|HazelcastQueueEndpointBuilderFactory
extends|,
name|HazelcastReplicatedmapEndpointBuilderFactory
extends|,
name|HazelcastRingbufferEndpointBuilderFactory
extends|,
name|HazelcastSedaEndpointBuilderFactory
extends|,
name|HazelcastSetEndpointBuilderFactory
extends|,
name|HazelcastTopicEndpointBuilderFactory
extends|,
name|HdfsEndpointBuilderFactory
extends|,
name|HipchatEndpointBuilderFactory
extends|,
name|HttpEndpointBuilderFactory
extends|,
name|IAMEndpointBuilderFactory
extends|,
name|IOTAEndpointBuilderFactory
extends|,
name|IPFSEndpointBuilderFactory
extends|,
name|IgniteCacheEndpointBuilderFactory
extends|,
name|IgniteComputeEndpointBuilderFactory
extends|,
name|IgniteEventsEndpointBuilderFactory
extends|,
name|IgniteIdGenEndpointBuilderFactory
extends|,
name|IgniteMessagingEndpointBuilderFactory
extends|,
name|IgniteQueueEndpointBuilderFactory
extends|,
name|IgniteSetEndpointBuilderFactory
extends|,
name|InfinispanEndpointBuilderFactory
extends|,
name|InfluxDbEndpointBuilderFactory
extends|,
name|IrcEndpointBuilderFactory
extends|,
name|IronMQEndpointBuilderFactory
extends|,
name|JBPMEndpointBuilderFactory
extends|,
name|JCacheEndpointBuilderFactory
extends|,
name|JGroupsEndpointBuilderFactory
extends|,
name|JGroupsRaftEndpointBuilderFactory
extends|,
name|JMXEndpointBuilderFactory
extends|,
name|JSR356WebSocketEndpointBuilderFactory
extends|,
name|JcloudsEndpointBuilderFactory
extends|,
name|JcrEndpointBuilderFactory
extends|,
name|JdbcEndpointBuilderFactory
extends|,
name|JettyHttpEndpointBuilder9Factory
extends|,
name|JingEndpointBuilderFactory
extends|,
name|JiraEndpointBuilderFactory
extends|,
name|JmsEndpointBuilderFactory
extends|,
name|JoltEndpointBuilderFactory
extends|,
name|JooqEndpointBuilderFactory
extends|,
name|JpaEndpointBuilderFactory
extends|,
name|JsonValidatorEndpointBuilderFactory
extends|,
name|Jt400EndpointBuilderFactory
extends|,
name|KMSEndpointBuilderFactory
extends|,
name|KafkaEndpointBuilderFactory
extends|,
name|KeystoneEndpointBuilderFactory
extends|,
name|KinesisEndpointBuilderFactory
extends|,
name|KinesisFirehoseEndpointBuilderFactory
extends|,
name|KubernetesConfigMapsEndpointBuilderFactory
extends|,
name|KubernetesDeploymentsEndpointBuilderFactory
extends|,
name|KubernetesHPAEndpointBuilderFactory
extends|,
name|KubernetesJobEndpointBuilderFactory
extends|,
name|KubernetesNamespacesEndpointBuilderFactory
extends|,
name|KubernetesNodesEndpointBuilderFactory
extends|,
name|KubernetesPersistentVolumesClaimsEndpointBuilderFactory
extends|,
name|KubernetesPersistentVolumesEndpointBuilderFactory
extends|,
name|KubernetesPodsEndpointBuilderFactory
extends|,
name|KubernetesReplicationControllersEndpointBuilderFactory
extends|,
name|KubernetesResourcesQuotaEndpointBuilderFactory
extends|,
name|KubernetesSecretsEndpointBuilderFactory
extends|,
name|KubernetesServiceAccountsEndpointBuilderFactory
extends|,
name|KubernetesServicesEndpointBuilderFactory
extends|,
name|LambdaEndpointBuilderFactory
extends|,
name|LanguageEndpointBuilderFactory
extends|,
name|LdapEndpointBuilderFactory
extends|,
name|LdifEndpointBuilderFactory
extends|,
name|LinkedInEndpointBuilderFactory
extends|,
name|LogEndpointBuilderFactory
extends|,
name|LuceneEndpointBuilderFactory
extends|,
name|LumberjackEndpointBuilderFactory
extends|,
name|MQEndpointBuilderFactory
extends|,
name|MQTTEndpointBuilderFactory
extends|,
name|MSKEndpointBuilderFactory
extends|,
name|MailEndpointBuilderFactory
extends|,
name|MasterEndpointBuilderFactory
extends|,
name|MetricsEndpointBuilderFactory
extends|,
name|MicrometerEndpointBuilderFactory
extends|,
name|MiloClientEndpointBuilderFactory
extends|,
name|MiloServerEndpointBuilderFactory
extends|,
name|MinaEndpointBuilderFactory
extends|,
name|MllpEndpointBuilderFactory
extends|,
name|MockEndpointBuilderFactory
extends|,
name|MongoDbEndpointBuilderFactory
extends|,
name|MsvEndpointBuilderFactory
extends|,
name|MustacheEndpointBuilderFactory
extends|,
name|MvelEndpointBuilderFactory
extends|,
name|MyBatisBeanEndpointBuilderFactory
extends|,
name|MyBatisEndpointBuilderFactory
extends|,
name|NagiosEndpointBuilderFactory
extends|,
name|NatsEndpointBuilderFactory
extends|,
name|NetWeaverEndpointBuilderFactory
extends|,
name|NettyEndpointBuilderFactory
extends|,
name|NettyHttpEndpointBuilderFactory
extends|,
name|NeutronEndpointBuilderFactory
extends|,
name|NovaEndpointBuilderFactory
extends|,
name|NsqEndpointBuilderFactory
extends|,
name|Olingo2EndpointBuilderFactory
extends|,
name|Olingo4EndpointBuilderFactory
extends|,
name|OpenshiftBuildConfigsEndpointBuilderFactory
extends|,
name|OpenshiftBuildsEndpointBuilderFactory
extends|,
name|OptaPlannerEndpointBuilderFactory
extends|,
name|PahoEndpointBuilderFactory
extends|,
name|PaxLoggingEndpointBuilderFactory
extends|,
name|PdfEndpointBuilderFactory
extends|,
name|PgEventEndpointBuilderFactory
extends|,
name|PrinterEndpointBuilderFactory
extends|,
name|PubNubEndpointBuilderFactory
extends|,
name|PulsarEndpointBuilderFactory
extends|,
name|QuartzEndpointBuilderFactory
extends|,
name|QueueServiceEndpointBuilderFactory
extends|,
name|QuickfixjEndpointBuilderFactory
extends|,
name|RabbitMQEndpointBuilderFactory
extends|,
name|ReactiveStreamsEndpointBuilderFactory
extends|,
name|RedisEndpointBuilderFactory
extends|,
name|RefEndpointBuilderFactory
extends|,
name|RestApiEndpointBuilderFactory
extends|,
name|RestEndpointBuilderFactory
extends|,
name|RestSwaggerEndpointBuilderFactory
extends|,
name|RestletEndpointBuilderFactory
extends|,
name|RssEndpointBuilderFactory
extends|,
name|S3EndpointBuilderFactory
extends|,
name|SWFEndpointBuilderFactory
extends|,
name|SagaEndpointBuilderFactory
extends|,
name|SalesforceEndpointBuilderFactory
extends|,
name|SchedulerEndpointBuilderFactory
extends|,
name|SchematronEndpointBuilderFactory
extends|,
name|ScpEndpointBuilderFactory
extends|,
name|SdbEndpointBuilderFactory
extends|,
name|SedaEndpointBuilderFactory
extends|,
name|ServerEndpointBuilderFactory
extends|,
name|ServiceEndpointBuilderFactory
extends|,
name|ServiceNowEndpointBuilderFactory
extends|,
name|ServletEndpointBuilderFactory
extends|,
name|SesEndpointBuilderFactory
extends|,
name|SftpEndpointBuilderFactory
extends|,
name|SipEndpointBuilderFactory
extends|,
name|Sjms2EndpointBuilderFactory
extends|,
name|SjmsBatchEndpointBuilderFactory
extends|,
name|SjmsEndpointBuilderFactory
extends|,
name|SlackEndpointBuilderFactory
extends|,
name|SmppEndpointBuilderFactory
extends|,
name|SnmpEndpointBuilderFactory
extends|,
name|SnsEndpointBuilderFactory
extends|,
name|SolrEndpointBuilderFactory
extends|,
name|SoroushBotEndpointBuilderFactory
extends|,
name|SparkEndpointBuilderFactory
extends|,
name|SplunkEndpointBuilderFactory
extends|,
name|SpringBatchEndpointBuilderFactory
extends|,
name|SpringIntegrationEndpointBuilderFactory
extends|,
name|SpringLdapEndpointBuilderFactory
extends|,
name|SpringWebserviceEndpointBuilderFactory
extends|,
name|SqlEndpointBuilderFactory
extends|,
name|SqlStoredEndpointBuilderFactory
extends|,
name|SqsEndpointBuilderFactory
extends|,
name|SshEndpointBuilderFactory
extends|,
name|StAXEndpointBuilderFactory
extends|,
name|StompEndpointBuilderFactory
extends|,
name|StreamEndpointBuilderFactory
extends|,
name|StringTemplateEndpointBuilderFactory
extends|,
name|StubEndpointBuilderFactory
extends|,
name|SwiftEndpointBuilderFactory
extends|,
name|TelegramEndpointBuilderFactory
extends|,
name|ThriftEndpointBuilderFactory
extends|,
name|TikaEndpointBuilderFactory
extends|,
name|TimerEndpointBuilderFactory
extends|,
name|TwilioEndpointBuilderFactory
extends|,
name|TwitterDirectMessageEndpointBuilderFactory
extends|,
name|TwitterSearchEndpointBuilderFactory
extends|,
name|TwitterTimelineEndpointBuilderFactory
extends|,
name|UndertowEndpointBuilderFactory
extends|,
name|ValidatorEndpointBuilderFactory
extends|,
name|VelocityEndpointBuilderFactory
extends|,
name|VertxEndpointBuilderFactory
extends|,
name|VmEndpointBuilderFactory
extends|,
name|WeatherEndpointBuilderFactory
extends|,
name|Web3jEndpointBuilderFactory
extends|,
name|WebhookEndpointBuilderFactory
extends|,
name|WebsocketEndpointBuilderFactory
extends|,
name|WordpressEndpointBuilderFactory
extends|,
name|WsEndpointBuilderFactory
extends|,
name|XChangeEndpointBuilderFactory
extends|,
name|XQueryEndpointBuilderFactory
extends|,
name|XmlSignatureEndpointBuilderFactory
extends|,
name|XmppEndpointBuilderFactory
extends|,
name|XsltEndpointBuilderFactory
extends|,
name|YammerEndpointBuilderFactory
extends|,
name|ZendeskEndpointBuilderFactory
extends|,
name|ZooKeeperEndpointBuilderFactory
extends|,
name|ZooKeeperMasterEndpointBuilderFactory
block|{
DECL|method|endpoints (EndpointProducerBuilder... endpoints)
specifier|default
name|Expression
name|endpoints
parameter_list|(
name|EndpointProducerBuilder
modifier|...
name|endpoints
parameter_list|)
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
init|=
name|Stream
operator|.
name|of
argument_list|(
name|endpoints
argument_list|)
operator|.
name|map
argument_list|(
name|EndpointProducerBuilder
operator|::
name|expr
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|expressions
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_interface

end_unit

